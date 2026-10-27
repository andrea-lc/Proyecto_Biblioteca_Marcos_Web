package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.SancionDtos.Request;
import com.bibliot.gestion_biblioteca.dto.SancionDtos.Response;
import com.bibliot.gestion_biblioteca.service.SancionService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/api/bibliotecario/sanciones")
public class SancionController {
    private final SancionService service;

    public SancionController(SancionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Response> listar(@RequestParam(required = false) String estado) {
        var xs = estado == null ? service.listarTodos() : service.buscarPorEstado(estado);
        return xs.stream().map(Response::desde).toList();
    }

    @GetMapping("/{id}")
    public Response buscar(@PathVariable Integer id) {
        return Response.desde(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Response> crear(@Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdSancion(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.desde(service.guardar(x)));
    }

    @PutMapping("/{id}")
    public Response actualizar(@PathVariable Integer id, @Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdSancion(id);
        return Response.desde(service.guardar(x));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
