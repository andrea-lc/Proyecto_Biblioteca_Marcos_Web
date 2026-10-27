package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.ApiDtos.LibroDigitalDto;
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
    public List<LibroDigitalDto> listar() {
        return service.listarTodos().stream().map(LibroDigitalDto::from).toList();
    }

    @GetMapping("/{id}")
    public LibroDigitalDto buscar(@PathVariable Integer id) {
        return LibroDigitalDto.from(service.buscarPorId(id));
    }

    @GetMapping("/libro/{idLibro}")
    public LibroDigitalDto porLibro(@PathVariable Integer idLibro) {
        return LibroDigitalDto.from(service.buscarPorLibro(idLibro));
    }

    @PostMapping
    public ResponseEntity<LibroDigitalDto> crear(@Valid @RequestBody LibroDigitalDto d) {
        var x = d.toEntity();
        x.setIdLibroDigital(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(LibroDigitalDto.from(service.guardar(x)));
    }

    @PutMapping("/{id}")
    public LibroDigitalDto actualizar(@PathVariable Integer id, @Valid @RequestBody LibroDigitalDto d) {
        var x = d.toEntity();
        x.setIdLibroDigital(id);
        return LibroDigitalDto.from(service.guardar(x));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
