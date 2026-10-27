package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.ApiDtos.LibroAutorDto;
import com.bibliot.gestion_biblioteca.service.LibroAutorService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/api/admin/libros-autores")
public class LibroAutorController {
    private final LibroAutorService service;

    public LibroAutorController(LibroAutorService service) {
        this.service = service;
    }

    @GetMapping
    public List<LibroAutorDto> listar() {
        return service.listar().stream().map(LibroAutorDto::from).toList();
    }

    @GetMapping("/libro/{idLibro}")
    public List<LibroAutorDto> delLibro(@PathVariable Integer idLibro) {
        return service.buscarPorLibro(idLibro).stream().map(LibroAutorDto::from).toList();
    }

    @GetMapping("/{idLibro}/{idAutor}")
    public LibroAutorDto buscar(@PathVariable Integer idLibro, @PathVariable Integer idAutor) {
        return LibroAutorDto.from(service.buscar(idLibro, idAutor));
    }

    @PostMapping
    public ResponseEntity<LibroAutorDto> crear(@Valid @RequestBody LibroAutorDto d) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(LibroAutorDto.from(service.guardar(d.idLibro(), d.idAutor())));
    }

    @PutMapping("/{idLibro}/{idAutor}")
    public LibroAutorDto actualizar(@PathVariable Integer idLibro, @PathVariable Integer idAutor,
            @Valid @RequestBody LibroAutorDto d) {
        service.eliminar(idLibro, idAutor);
        return LibroAutorDto.from(service.guardar(d.idLibro(), d.idAutor()));
    }

    @DeleteMapping("/{idLibro}/{idAutor}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idLibro, @PathVariable Integer idAutor) {
        service.eliminar(idLibro, idAutor);
        return ResponseEntity.noContent().build();
    }
}
