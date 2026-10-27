package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.LibroDtos.Response;
import com.bibliot.gestion_biblioteca.service.LibroService;
import com.bibliot.gestion_biblioteca.service.LibroResponseAssembler;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RestController
@Transactional(readOnly = true)
@RequestMapping("/api/catalogo")
public class CatalogoController {
    private final LibroService service;
    private final LibroResponseAssembler assembler;

    public CatalogoController(LibroService service, LibroResponseAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
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
        return assembler.desdeTodos(service.listarTodos()).stream()
                .filter(l -> q == null || q.isBlank() || l.titulo().toLowerCase().contains(q.toLowerCase())
                        || l.autores().stream().anyMatch(a -> a.toLowerCase().contains(q.toLowerCase())))
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

    @GetMapping("/buscar")
    public List<Response> buscarTitulo(@RequestParam String titulo) {
        return assembler.desdeTodos(service.buscarPorTitulo(titulo));
    }
}
