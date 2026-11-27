package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.AutorDtos.Request;
import com.bibliot.gestion_biblioteca.dto.AutorDtos.Response;
import com.bibliot.gestion_biblioteca.dto.CatalogoAdminDtos.Autor;
import com.bibliot.gestion_biblioteca.repository.LibroAutorRepository;
import com.bibliot.gestion_biblioteca.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/autores")
public class AutorController {
    private final AutorService service;
    private final LibroAutorRepository librosAutores;

    public AutorController(AutorService service, LibroAutorRepository librosAutores) {
        this.service = service;
        this.librosAutores = librosAutores;
    }

    @GetMapping
    public List<Response> listar(@RequestParam(required = false) String nombre) {
        var xs = nombre == null ? service.listarTodos() : service.buscarPorNombre(nombre);
        return xs.stream().map(Response::desde).toList();
    }

    @GetMapping("/con-libros")
    public List<Autor> listarConLibros() {
        Map<Integer, Long> conteos = librosAutores.contarLibrosPorAutor().stream()
                .collect(Collectors.toMap(row -> ((Number) row[0]).intValue(), row -> ((Number) row[1]).longValue()));
        return service.listarTodos().stream()
                .map(autor -> new Autor(autor.getIdAutor(), autor.getNombres(), autor.getApellidos(),
                        autor.getNacionalidad(), autor.getBiografia(), conteos.getOrDefault(autor.getIdAutor(), 0L)))
                .toList();
    }

    @GetMapping("/{id}")
    public Response buscar(@PathVariable Integer id) {
        return Response.desde(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Response> crear(@Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdAutor(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.desde(service.guardar(x)));
    }

    @PutMapping("/{id}")
    public Response actualizar(@PathVariable Integer id, @Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdAutor(id);
        return Response.desde(service.guardar(x));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
