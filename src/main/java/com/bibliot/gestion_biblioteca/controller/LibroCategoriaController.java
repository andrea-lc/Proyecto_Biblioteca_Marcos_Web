package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.ApiDtos.LibroCategoriaDto;
import com.bibliot.gestion_biblioteca.service.LibroCategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/api/admin/libros-categorias")
public class LibroCategoriaController {
    private final LibroCategoriaService service;

    public LibroCategoriaController(LibroCategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public List<LibroCategoriaDto> listar() {
        return service.listar().stream().map(LibroCategoriaDto::from).toList();
    }

    @GetMapping("/libro/{idLibro}")
    public List<LibroCategoriaDto> delLibro(@PathVariable Integer idLibro) {
        return service.buscarPorLibro(idLibro).stream().map(LibroCategoriaDto::from).toList();
    }

    @GetMapping("/{idLibro}/{idCategoria}")
    public LibroCategoriaDto buscar(@PathVariable Integer idLibro, @PathVariable Integer idCategoria) {
        return LibroCategoriaDto.from(service.buscar(idLibro, idCategoria));
    }

    @PostMapping
    public ResponseEntity<LibroCategoriaDto> crear(@Valid @RequestBody LibroCategoriaDto d) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(LibroCategoriaDto.from(service.guardar(d.idLibro(), d.idCategoria())));
    }

    @PutMapping("/{idLibro}/{idCategoria}")
    public LibroCategoriaDto actualizar(@PathVariable Integer idLibro, @PathVariable Integer idCategoria,
            @Valid @RequestBody LibroCategoriaDto d) {
        service.eliminar(idLibro, idCategoria);
        return LibroCategoriaDto.from(service.guardar(d.idLibro(), d.idCategoria()));
    }

    @DeleteMapping("/{idLibro}/{idCategoria}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idLibro, @PathVariable Integer idCategoria) {
        service.eliminar(idLibro, idCategoria);
        return ResponseEntity.noContent().build();
    }
}
