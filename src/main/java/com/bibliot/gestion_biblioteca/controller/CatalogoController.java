package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.LibroDtos.Response;
import com.bibliot.gestion_biblioteca.service.LibroService;
import com.bibliot.gestion_biblioteca.service.LibroResponseAssembler;
import com.bibliot.gestion_biblioteca.repository.LibroDigitalRepository;
import com.bibliot.gestion_biblioteca.service.LibroArchivoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Locale;

@RestController
@Transactional(readOnly = true)
@RequestMapping("/api/catalogo")
public class CatalogoController {
    private final LibroService service;
    private final LibroResponseAssembler assembler;
    private final LibroDigitalRepository librosDigitales;
    private final LibroArchivoService archivos;

    public CatalogoController(LibroService service, LibroResponseAssembler assembler,
            LibroDigitalRepository librosDigitales, LibroArchivoService archivos) {
        this.service = service;
        this.assembler = assembler;
        this.librosDigitales = librosDigitales;
        this.archivos = archivos;
    }

    @GetMapping("/libros")
    public List<Response> listar(@RequestParam(required = false) String q,
            @RequestParam(required = false) Integer autorId,
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String paginas,
            @RequestParam(required = false) Integer editorialId,
            @RequestParam(required = false) Integer anioDesde,
            @RequestParam(required = false) Integer anioHasta) {
        List<Response> candidatos;
        if (q == null || q.isBlank()) {
            candidatos = assembler.desdeTodos(service.listarTodos());
        } else {
            String consulta = q.trim().toLowerCase(Locale.ROOT);
            LinkedHashMap<Integer, Response> coincidencias = new LinkedHashMap<>();
            assembler.desdeTodos(service.buscarTextoCompleto(consulta))
                    .forEach(libro -> coincidencias.put(libro.idLibro(), libro));
            assembler.desdeTodos(service.listarTodos()).stream()
                    .filter(libro -> libro.titulo().toLowerCase(Locale.ROOT).contains(consulta)
                            || libro.autores().stream().anyMatch(autor -> autor.toLowerCase(Locale.ROOT).contains(consulta)))
                    .forEach(libro -> coincidencias.putIfAbsent(libro.idLibro(), libro));
            candidatos = List.copyOf(coincidencias.values());
        }

        return candidatos.stream()
                .filter(l -> autorId == null || l.autorIds().contains(autorId))
                .filter(l -> categoriaId == null || l.categoriaIds().contains(categoriaId))
                .filter(l -> categoria == null || categoria.isBlank() || l.categorias().stream()
                        .anyMatch(nombre -> nombre.toLowerCase().contains(categoria.toLowerCase())))
                .filter(l -> editorialId == null || editorialId.equals(l.idEditorial()))
                .filter(l -> paginas == null || paginas.isBlank() || switch (paginas) {
                    case "menos200" -> l.paginas() != null && l.paginas() < 200;
                    case "200a400" -> l.paginas() != null && l.paginas() >= 200 && l.paginas() <= 400;
                    case "mas400" -> l.paginas() != null && l.paginas() > 400;
                    default -> true;
                })
                .filter(l -> anioDesde == null || (l.fechaPublicacion() != null && l.fechaPublicacion().getYear() >= anioDesde))
                .filter(l -> anioHasta == null || (l.fechaPublicacion() != null && l.fechaPublicacion().getYear() <= anioHasta))
                .toList();
    }

    @GetMapping("/libros/{id}")
    public Response buscar(@PathVariable Integer id) { return assembler.desde(service.buscarPorId(id)); }

    @GetMapping(value = "/libros/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> pdfLocal(@PathVariable Integer id) {
        var digital = librosDigitales.findByLibroIdLibro(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Este libro no tiene un archivo digital asociado."));
        Resource recurso = archivos.abrirPdfLocal(digital.getArchivoUrl());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline().filename("libro-" + id + ".pdf").build().toString())
                .body(recurso);
    }

    @GetMapping("/buscar")
    public List<Response> buscarTitulo(@RequestParam String titulo) {
        return assembler.desdeTodos(service.buscarPorTitulo(titulo));
    }
}
