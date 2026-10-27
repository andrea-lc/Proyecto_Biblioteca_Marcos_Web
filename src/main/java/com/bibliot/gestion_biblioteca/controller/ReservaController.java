package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.ApiDtos.ReservaDto;
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
    public List<ReservaDto> listar() {
        return service.listarTodos().stream().map(ReservaDto::from).toList();
    }

    @GetMapping("/bibliotecario/reservas/libro/{idLibro}")
    public List<ReservaDto> porLibro(@PathVariable Integer idLibro) {
        return service.buscarPorLibro(idLibro).stream().map(ReservaDto::from).toList();
    }

    @GetMapping("/bibliotecario/reservas/{id}")
    public ReservaDto buscar(@PathVariable Integer id) {
        return ReservaDto.from(service.buscarPorId(id));
    }

    @PostMapping("/bibliotecario/reservas")
    public ResponseEntity<ReservaDto> crear(@Valid @RequestBody ReservaDto d) {
        var x = d.toEntity();
        x.setIdReserva(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(ReservaDto.from(service.guardar(x)));
    }

    @PutMapping("/bibliotecario/reservas/{id}")
    public ReservaDto actualizar(@PathVariable Integer id, @Valid @RequestBody ReservaDto d) {
        var x = d.toEntity();
        x.setIdReserva(id);
        return ReservaDto.from(service.guardar(x));
    }

    @DeleteMapping("/bibliotecario/reservas/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/lector/mis-reservas/{idUsuario}")
    public List<ReservaDto> delUsuario(@PathVariable Integer idUsuario) {
        return service.buscarPorUsuario(idUsuario).stream().map(ReservaDto::from).toList();
    }

    @PostMapping("/lector/reservar-libro")
    public ResponseEntity<ReservaDto> reservar(@Valid @RequestBody ReservaDto d) {
        var x = d.toEntity();
        x.setIdReserva(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(ReservaDto.from(service.guardar(x)));
    }

    @DeleteMapping("/lector/reservas/{idReserva}")
    public ResponseEntity<Void> cancelar(@PathVariable Integer idReserva) {
        service.eliminar(idReserva);
        return ResponseEntity.noContent().build();
    }
}
