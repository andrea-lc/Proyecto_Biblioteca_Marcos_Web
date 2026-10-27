package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.ApiDtos.PrestamoDto;
import com.bibliot.gestion_biblioteca.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/api")
public class PrestamoController {
    private final PrestamoService service;

    public PrestamoController(PrestamoService service) {
        this.service = service;
    }

    @GetMapping("/bibliotecario/prestamos")
    public List<PrestamoDto> listar() {
        return service.listarTodos().stream().map(PrestamoDto::from).toList();
    }

    @GetMapping("/bibliotecario/prestamos/activos")
    public List<PrestamoDto> activos() {
        return service.buscarPrestamosActivos().stream().map(PrestamoDto::from).toList();
    }

    @GetMapping("/bibliotecario/prestamos/{id}")
    public PrestamoDto buscar(@PathVariable Integer id) {
        return PrestamoDto.from(service.buscarPorId(id));
    }

    @PostMapping("/bibliotecario/prestamos")
    public ResponseEntity<PrestamoDto> crear(@Valid @RequestBody PrestamoDto d) {
        var x = d.toEntity();
        x.setIdPrestamo(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(PrestamoDto.from(service.guardar(x)));
    }

    @PutMapping("/bibliotecario/prestamos/{id}")
    public PrestamoDto actualizar(@PathVariable Integer id, @Valid @RequestBody PrestamoDto d) {
        var x = d.toEntity();
        x.setIdPrestamo(id);
        return PrestamoDto.from(service.guardar(x));
    }

    @DeleteMapping("/bibliotecario/prestamos/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/lector/mis-prestamos/{idUsuario}")
    public List<PrestamoDto> delUsuario(@PathVariable Integer idUsuario) {
        return service.buscarPorUsuario(idUsuario).stream().map(PrestamoDto::from).toList();
    }

    @PostMapping("/lector/solicitar-prestamo")
    public ResponseEntity<PrestamoDto> solicitar(@Valid @RequestBody PrestamoDto d) {
        var x = d.toEntity();
        x.setIdPrestamo(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(PrestamoDto.from(service.guardar(x)));
    }
}
