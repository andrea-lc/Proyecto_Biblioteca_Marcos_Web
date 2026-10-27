package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.EjemplarDtos.Request;
import com.bibliot.gestion_biblioteca.dto.EjemplarDtos.Response;
import com.bibliot.gestion_biblioteca.service.EjemplarService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RestController
@Transactional
@RequestMapping({ "/api/admin/ejemplares", "/api/bibliotecario/ejemplares" })
public class EjemplarController {
    private final EjemplarService service;

    public EjemplarController(EjemplarService service) {
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

    @GetMapping("/libro/{idLibro}")
    public List<Response> porLibro(@PathVariable Integer idLibro) {
        return service.buscarPorLibro(idLibro).stream().map(Response::desde).toList();
    }

    @GetMapping("/codigo/{codigo}")
    public Response codigo(@PathVariable String codigo) {
        return Response.desde(service.buscarPorCodigoInventario(codigo));
    }

    @PostMapping
    public ResponseEntity<Response> crear(@Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdEjemplar(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.desde(service.guardar(x)));
    }

    @PutMapping("/{id}")
    public Response actualizar(@PathVariable Integer id, @Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdEjemplar(id);
        return Response.desde(service.guardar(x));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
