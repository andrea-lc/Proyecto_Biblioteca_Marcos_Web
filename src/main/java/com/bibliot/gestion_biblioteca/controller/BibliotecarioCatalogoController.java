package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.LibroDtos.Response;
import com.bibliot.gestion_biblioteca.service.LibroService;
import com.bibliot.gestion_biblioteca.service.LibroResponseAssembler;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RestController
@Transactional(readOnly = true)
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

    @GetMapping("/{id}")
    public Response buscar(@PathVariable Integer id) {
        return assembler.desde(service.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public List<Response> buscarTitulo(@RequestParam String titulo) {
        return assembler.desdeTodos(service.buscarPorTitulo(titulo));
    }
}
