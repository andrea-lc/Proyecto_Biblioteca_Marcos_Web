package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.LibroCategoriaDtos.Request;
import com.bibliot.gestion_biblioteca.dto.LibroCategoriaDtos.Response;
import com.bibliot.gestion_biblioteca.service.LibroCategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/api/admin/libros-categorias")
public class LibroCategoriaController {
    private final LibroCategoriaService service;

    public LibroCategoriaController(LibroCategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Response> listar() {
        return service.listar().stream().map(Response::desde).toList();
    }

    @GetMapping("/libro/{idLibro}")
    public List<Response> delLibro(@PathVariable Integer idLibro) {
        return service.buscarPorLibro(idLibro).stream().map(Response::desde).toList();
    }

    @GetMapping("/{idLibro}/{idCategoria}")
    public Response buscar(@PathVariable Integer idLibro, @PathVariable Integer idCategoria) {
        return Response.desde(service.buscar(idLibro, idCategoria));
    }

    @PostMapping
    public ResponseEntity<Response> crear(@Valid @RequestBody Request d) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.desde(service.guardar(d.idLibro(), d.idCategoria())));
    }

    @PutMapping("/{idLibro}/{idCategoria}")
    public Response actualizar(@PathVariable Integer idLibro, @PathVariable Integer idCategoria,
            @Valid @RequestBody Request d) {
        service.eliminar(idLibro, idCategoria);
        return Response.desde(service.guardar(d.idLibro(), d.idCategoria()));
    }

    @DeleteMapping("/{idLibro}/{idCategoria}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idLibro, @PathVariable Integer idCategoria) {
        service.eliminar(idLibro, idCategoria);
        return ResponseEntity.noContent().build();
    }
}
