package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.LibroDtos.Response;
import com.bibliot.gestion_biblioteca.dto.LibroDtos.PageResponse;
import com.bibliot.gestion_biblioteca.entities.Libro;
import com.bibliot.gestion_biblioteca.service.LibroService;
import com.bibliot.gestion_biblioteca.service.LibroResponseAssembler;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

@RestController
@Transactional(readOnly = true)
@Validated
@RequestMapping("/api/bibliotecario/libros")
public class BibliotecarioCatalogoController {
    private final LibroService service;
    private final LibroResponseAssembler assembler;

    public BibliotecarioCatalogoController(LibroService service, LibroResponseAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping
    public List<Response> listar() {
        return assembler.desdeTodos(service.listarTodos());
    }

    @GetMapping("/pagina")
    public PageResponse listarPagina(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) @Min(1) Integer autorId,
            @RequestParam(required = false) @Min(1) Integer editorialId,
            @RequestParam(required = false) @Min(1) Integer categoriaId,
            @RequestParam(required = false) @Size(max = 30) String estado,
            @RequestParam(required = false) @Pattern(regexp = "(?i)FISICO|DIGITAL|AMBOS|SIN_FORMATO") String formato) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "idLibro"));
        Page<Libro> resultado = service.buscarPagina(q, autorId, editorialId, categoriaId, estado, formato, pageable);
        return new PageResponse(
                assembler.desdeTodos(resultado.getContent()), resultado.getNumber(), resultado.getSize(),
                resultado.getTotalElements(), resultado.getTotalPages(), resultado.isFirst(), resultado.isLast());
    }

    @GetMapping("/{id}")
    public Response buscar(@PathVariable Integer id) {
        return assembler.desde(service.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public List<Response> buscarTitulo(@RequestParam String titulo) {
        return assembler.desdeTodos(service.buscarPorTitulo(titulo));
    }
}
