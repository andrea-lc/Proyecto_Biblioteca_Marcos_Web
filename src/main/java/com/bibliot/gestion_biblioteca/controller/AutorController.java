package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.AutorDtos.Request;
import com.bibliot.gestion_biblioteca.dto.AutorDtos.Response;
import com.bibliot.gestion_biblioteca.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/autores")
public class AutorController {
    private final AutorService service;

    public AutorController(AutorService service) {
        this.service = service;
    }

    @GetMapping
    public List<Response> listar(@RequestParam(required = false) String nombre) {
        var xs = nombre == null ? service.listarTodos() : service.buscarPorNombre(nombre);
        return xs.stream().map(Response::desde).toList();
    }

    @GetMapping("/{id}")
    public Response buscar(@PathVariable Integer id) {
        return Response.desde(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Response> crear(@Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdAutor(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.desde(service.guardar(x)));
    }

    @PutMapping("/{id}")
    public Response actualizar(@PathVariable Integer id, @Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdAutor(id);
        return Response.desde(service.guardar(x));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
