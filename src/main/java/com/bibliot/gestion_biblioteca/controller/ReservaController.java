package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.ReservaDtos.Request;
import com.bibliot.gestion_biblioteca.dto.ReservaDtos.Response;
import com.bibliot.gestion_biblioteca.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/api")
public class ReservaController {
    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    @GetMapping("/bibliotecario/reservas")
    public List<Response> listar() {
        return service.listarTodos().stream().map(Response::desde).toList();
    }

    @GetMapping("/bibliotecario/reservas/libro/{idLibro}")
    public List<Response> porLibro(@PathVariable Integer idLibro) {
        return service.buscarPorLibro(idLibro).stream().map(Response::desde).toList();
    }

    @GetMapping("/bibliotecario/reservas/{id}")
    public Response buscar(@PathVariable Integer id) {
        return Response.desde(service.buscarPorId(id));
    }

    @PostMapping("/bibliotecario/reservas")
    public ResponseEntity<Response> crear(@Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdReserva(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.desde(service.guardar(x)));
    }

    @PutMapping("/bibliotecario/reservas/{id}")
    public Response actualizar(@PathVariable Integer id, @Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdReserva(id);
        return Response.desde(service.guardar(x));
    }

    @DeleteMapping("/bibliotecario/reservas/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/lector/mis-reservas/{idUsuario}")
    public List<Response> delUsuario(@PathVariable Integer idUsuario) {
        return service.buscarPorUsuario(idUsuario).stream().map(Response::desde).toList();
    }

    @PostMapping("/lector/reservar-libro")
    public ResponseEntity<Response> reservar(@Valid @RequestBody Request d) {
        var x = d.toEntity();
        x.setIdReserva(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.desde(service.guardar(x)));
    }

    @DeleteMapping("/lector/reservas/{idReserva}")
    public ResponseEntity<Void> cancelar(@PathVariable Integer idReserva) {
        service.eliminar(idReserva);
        return ResponseEntity.noContent().build();
    }
}
