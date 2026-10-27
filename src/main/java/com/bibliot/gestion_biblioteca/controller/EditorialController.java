package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.ApiDtos.EditorialDto;
import com.bibliot.gestion_biblioteca.service.EditorialService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/editoriales")
public class EditorialController {
    private final EditorialService service;

    public EditorialController(EditorialService service) {
        this.service = service;
    }

    @GetMapping
    public List<EditorialDto> listar() {
        return service.listarTodos().stream().map(EditorialDto::from).toList();
    }

    @GetMapping("/{id}")
    public EditorialDto buscar(@PathVariable Integer id) {
        return EditorialDto.from(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<EditorialDto> crear(@Valid @RequestBody EditorialDto d) {
        var x = d.toEntity();
        x.setIdEditorial(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(EditorialDto.from(service.guardar(x)));
    }

    @PutMapping("/{id}")
    public EditorialDto actualizar(@PathVariable Integer id, @Valid @RequestBody EditorialDto d) {
        var x = d.toEntity();
        x.setIdEditorial(id);
        return EditorialDto.from(service.guardar(x));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
