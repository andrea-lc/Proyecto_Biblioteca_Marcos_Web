package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.ApiDtos.AutorDto;
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
    public List<AutorDto> listar(@RequestParam(required = false) String nombre) {
        var xs = nombre == null ? service.listarTodos() : service.buscarPorNombre(nombre);
        return xs.stream().map(AutorDto::from).toList();
    }

    @GetMapping("/{id}")
    public AutorDto buscar(@PathVariable Integer id) {
        return AutorDto.from(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AutorDto> crear(@Valid @RequestBody AutorDto d) {
        var x = d.toEntity();
        x.setIdAutor(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(AutorDto.from(service.guardar(x)));
    }

    @PutMapping("/{id}")
    public AutorDto actualizar(@PathVariable Integer id, @Valid @RequestBody AutorDto d) {
        var x = d.toEntity();
        x.setIdAutor(id);
        return AutorDto.from(service.guardar(x));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
