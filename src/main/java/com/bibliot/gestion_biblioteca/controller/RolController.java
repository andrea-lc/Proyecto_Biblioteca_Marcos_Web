package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.RolDtos.Request;
import com.bibliot.gestion_biblioteca.dto.RolDtos.Response;
import com.bibliot.gestion_biblioteca.service.RolService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/roles")
public class RolController {
    private final RolService service;

    public RolController(RolService service) {
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

    @GetMapping("/nombre/{nombre}")
    public Response porNombre(@PathVariable String nombre) {
        return Response.desde(service.buscarPorNombre(nombre));
    }

    @PostMapping
    public ResponseEntity<Response> crear(@Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdRol(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.desde(service.guardar(x)));
    }

    @PutMapping("/{id}")
    public Response actualizar(@PathVariable Integer id, @Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdRol(id);
        return Response.desde(service.guardar(x));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
