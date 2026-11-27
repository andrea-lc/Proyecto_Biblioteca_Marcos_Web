package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.LibroDigitalDtos.Request;
import com.bibliot.gestion_biblioteca.dto.LibroDigitalDtos.Response;
import com.bibliot.gestion_biblioteca.service.LibroDigitalService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/api/admin/libros-digitales")
public class LibroDigitalController {
    private final LibroDigitalService service;

    public LibroDigitalController(LibroDigitalService service) {
        this.service = service;
    }

    @GetMapping
    public List<Response> listar() {
        return service.listarTodos().stream().map(Response::desde).toList();
    }

    @GetMapping("/{id}")
    public Response buscar(@PathVariable Integer id) {
        return Response.desde(service.buscarPorId(id));
    }

    @GetMapping("/libro/{idLibro}")
    public Response porLibro(@PathVariable Integer idLibro) {
        return Response.desde(service.buscarPorLibro(idLibro));
    }

    @PostMapping
    public ResponseEntity<Response> crear(@Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdLibroDigital(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.desde(service.guardar(x)));
    }

    @PutMapping("/{id}")
    public Response actualizar(@PathVariable Integer id, @Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdLibroDigital(id);
        return Response.desde(service.guardar(x));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
