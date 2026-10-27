package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.ApiDtos.CategoriaDto;
import com.bibliot.gestion_biblioteca.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/categorias")
public class CategoriaController {
    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoriaDto> listar() {
        return service.listarTodos().stream().map(CategoriaDto::from).toList();
    }

    @GetMapping("/{id}")
    public CategoriaDto buscar(@PathVariable Integer id) {
        return CategoriaDto.from(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaDto> crear(@Valid @RequestBody CategoriaDto d) {
        var x = d.toEntity();
        x.setIdCategoria(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoriaDto.from(service.guardar(x)));
    }

    @PutMapping("/{id}")
    public CategoriaDto actualizar(@PathVariable Integer id, @Valid @RequestBody CategoriaDto d) {
        var x = d.toEntity();
        x.setIdCategoria(id);
        return CategoriaDto.from(service.guardar(x));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
