package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.ApiDtos.RolDto;
import com.bibliot.gestion_biblioteca.service.RolService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/roles")
public class RolController {
    private final RolService service;

    public RolController(RolService service) {
        this.service = service;
    }

    @GetMapping
    public List<RolDto> listar() {
        return service.listarTodos().stream().map(RolDto::from).toList();
    }

    @GetMapping("/{id}")
    public RolDto buscar(@PathVariable Integer id) {
        return RolDto.from(service.buscarPorId(id));
    }

    @GetMapping("/nombre/{nombre}")
    public RolDto porNombre(@PathVariable String nombre) {
        return RolDto.from(service.buscarPorNombre(nombre));
    }

    @PostMapping
    public ResponseEntity<RolDto> crear(@Valid @RequestBody RolDto d) {
        var x = d.toEntity();
        x.setIdRol(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(RolDto.from(service.guardar(x)));
    }

    @PutMapping("/{id}")
    public RolDto actualizar(@PathVariable Integer id, @Valid @RequestBody RolDto d) {
        var x = d.toEntity();
        x.setIdRol(id);
        return RolDto.from(service.guardar(x));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
