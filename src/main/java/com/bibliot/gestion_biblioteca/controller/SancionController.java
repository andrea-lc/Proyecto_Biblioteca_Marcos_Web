package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.ApiDtos.SancionDto;
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
    public List<SancionDto> listar(@RequestParam(required = false) String estado) {
        var xs = estado == null ? service.listarTodos() : service.buscarPorEstado(estado);
        return xs.stream().map(SancionDto::from).toList();
    }

    @GetMapping("/{id}")
    public SancionDto buscar(@PathVariable Integer id) {
        return SancionDto.from(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<SancionDto> crear(@Valid @RequestBody SancionDto d) {
        var x = d.toEntity();
        x.setIdSancion(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(SancionDto.from(service.guardar(x)));
    }

    @PutMapping("/{id}")
    public SancionDto actualizar(@PathVariable Integer id, @Valid @RequestBody SancionDto d) {
        var x = d.toEntity();
        x.setIdSancion(id);
        return SancionDto.from(service.guardar(x));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
