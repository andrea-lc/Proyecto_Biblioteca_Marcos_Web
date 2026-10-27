package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.PrestamoDtos.Request;
import com.bibliot.gestion_biblioteca.dto.PrestamoDtos.Response;
import com.bibliot.gestion_biblioteca.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/api")
public class PrestamoController {
    private final PrestamoService service;

    public PrestamoController(PrestamoService service) {
        this.service = service;
    }

    @GetMapping("/bibliotecario/prestamos")
    public List<Response> listar() {
        return service.listarTodos().stream().map(Response::desde).toList();
    }

    @GetMapping("/bibliotecario/prestamos/activos")
    public List<Response> activos() {
        return service.buscarPrestamosActivos().stream().map(Response::desde).toList();
    }

    @GetMapping("/bibliotecario/prestamos/{id}")
    public Response buscar(@PathVariable Integer id) {
        return Response.desde(service.buscarPorId(id));
    }

    @PostMapping("/bibliotecario/prestamos")
    public ResponseEntity<Response> crear(@Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdPrestamo(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.desde(service.guardar(x)));
    }

    @PutMapping("/bibliotecario/prestamos/{id}")
    public Response actualizar(@PathVariable Integer id, @Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdPrestamo(id);
        return Response.desde(service.guardar(x));
    }

    @DeleteMapping("/bibliotecario/prestamos/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/lector/mis-prestamos/{idUsuario}")
    public List<Response> delUsuario(@PathVariable Integer idUsuario) {
        return service.buscarPorUsuario(idUsuario).stream().map(Response::desde).toList();
    }

    @PostMapping("/lector/solicitar-prestamo")
    public ResponseEntity<Response> solicitar(@Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdPrestamo(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.desde(service.guardar(x)));
    }
}
