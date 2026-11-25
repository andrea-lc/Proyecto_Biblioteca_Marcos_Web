package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.LibroAutorDtos.Request;
import com.bibliot.gestion_biblioteca.dto.LibroAutorDtos.Response;
import com.bibliot.gestion_biblioteca.service.LibroAutorService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/api/admin/libros-autores")
public class LibroAutorController {
    private final LibroAutorService service;

    public LibroAutorController(LibroAutorService service) {
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

    @GetMapping("/{idLibro}/{idAutor}")
    public Response buscar(@PathVariable Integer idLibro, @PathVariable Integer idAutor) {
        return Response.desde(service.buscar(idLibro, idAutor));
    }

    @PostMapping
    public ResponseEntity<Response> crear(@Valid @RequestBody Request d) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.desde(service.guardar(d.idLibro(), d.idAutor())));
    }

    @PutMapping("/{idLibro}/{idAutor}")
    public Response actualizar(@PathVariable Integer idLibro, @PathVariable Integer idAutor,
            @Valid @RequestBody Request d) {
        service.eliminar(idLibro, idAutor);
        return Response.desde(service.guardar(d.idLibro(), d.idAutor()));
    }

    @DeleteMapping("/{idLibro}/{idAutor}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idLibro, @PathVariable Integer idAutor) {
        service.eliminar(idLibro, idAutor);
        return ResponseEntity.noContent().build();
    }
}
