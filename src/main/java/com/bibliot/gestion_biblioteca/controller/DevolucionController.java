package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.ApiDtos.DevolucionDto;
import com.bibliot.gestion_biblioteca.service.DevolucionService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/api/bibliotecario/devoluciones")
public class DevolucionController {
    private final DevolucionService service;

    public DevolucionController(DevolucionService service) {
        this.service = service;
    }

    @GetMapping
    public List<DevolucionDto> listar() {
        return service.listarTodos().stream().map(DevolucionDto::from).toList();
    }

    @GetMapping("/{id}")
    public DevolucionDto buscar(@PathVariable Integer id) {
        return DevolucionDto.from(service.buscarPorId(id));
    }

    @GetMapping("/prestamo/{idPrestamo}")
    public DevolucionDto porPrestamo(@PathVariable Integer idPrestamo) {
        return DevolucionDto.from(service.buscarPorPrestamo(idPrestamo));
    }

    @PostMapping
    public ResponseEntity<DevolucionDto> crear(@Valid @RequestBody DevolucionDto d) {
        var x = d.toEntity();
        x.setIdDevolucion(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(DevolucionDto.from(service.guardar(x)));
    }

    @PutMapping("/{id}")
    public DevolucionDto actualizar(@PathVariable Integer id, @Valid @RequestBody DevolucionDto d) {
        var x = d.toEntity();
        x.setIdDevolucion(id);
        return DevolucionDto.from(service.guardar(x));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
