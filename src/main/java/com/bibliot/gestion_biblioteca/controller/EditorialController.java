package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.EditorialDtos.Request;
import com.bibliot.gestion_biblioteca.dto.EditorialDtos.Response;
import com.bibliot.gestion_biblioteca.dto.CatalogoAdminDtos.Editorial;
import com.bibliot.gestion_biblioteca.repository.LibroRepository;
import com.bibliot.gestion_biblioteca.service.EditorialService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/editoriales")
public class EditorialController {
    private final EditorialService service;
    private final LibroRepository libros;

    public EditorialController(EditorialService service, LibroRepository libros) {
        this.service = service;
        this.libros = libros;
    }

    @GetMapping
    public List<Response> listar() {
        return service.listarTodos().stream().map(Response::desde).toList();
    }

    @GetMapping("/con-libros")
    public List<Editorial> listarConLibros() {
        Map<Integer, Long> conteos = libros.contarLibrosPorEditorial().stream()
                .collect(Collectors.toMap(row -> ((Number) row[0]).intValue(), row -> ((Number) row[1]).longValue()));
        return service.listarTodos().stream()
                .map(editorial -> new Editorial(editorial.getIdEditorial(), editorial.getNombre(),
                        editorial.getPais(), editorial.getSitioWeb(),
                        conteos.getOrDefault(editorial.getIdEditorial(), 0L)))
                .toList();
    }

    @GetMapping("/{id}")
    public Response buscar(@PathVariable Integer id) {
        return Response.desde(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Response> crear(@Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdEditorial(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.desde(service.guardar(x)));
    }

    @PutMapping("/{id}")
    public Response actualizar(@PathVariable Integer id, @Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdEditorial(id);
        return Response.desde(service.guardar(x));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
