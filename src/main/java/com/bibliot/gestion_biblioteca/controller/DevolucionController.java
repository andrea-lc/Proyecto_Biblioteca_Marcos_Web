package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.DevolucionDtos.Request;
import com.bibliot.gestion_biblioteca.dto.DevolucionDtos.Response;
import com.bibliot.gestion_biblioteca.service.DevolucionService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/api/bibliotecario/devoluciones")
public class DevolucionController {
    private final DevolucionService service;

    public DevolucionController(DevolucionService service) {
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

    @GetMapping("/prestamo/{idPrestamo}")
    public Response porPrestamo(@PathVariable Integer idPrestamo) {
        return Response.desde(service.buscarPorPrestamo(idPrestamo));
    }

    @PostMapping
    public ResponseEntity<Response> crear(@Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdDevolucion(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.desde(service.guardar(x)));
    }

    @PutMapping("/{id}")
    public Response actualizar(@PathVariable Integer id, @Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdDevolucion(id);
        return Response.desde(service.guardar(x));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
