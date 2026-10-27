package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.ApiDtos.EjemplarDto;
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
    public List<EjemplarDto> listar() {
        return service.listarTodos().stream().map(EjemplarDto::from).toList();
    }

    @GetMapping("/{id}")
    public EjemplarDto buscar(@PathVariable Integer id) {
        return EjemplarDto.from(service.buscarPorId(id));
    }

    @GetMapping("/libro/{idLibro}")
    public List<EjemplarDto> porLibro(@PathVariable Integer idLibro) {
        return service.buscarPorLibro(idLibro).stream().map(EjemplarDto::from).toList();
    }

    @GetMapping("/codigo/{codigo}")
    public EjemplarDto codigo(@PathVariable String codigo) {
        return EjemplarDto.from(service.buscarPorCodigoInventario(codigo));
    }

    @PostMapping
    public ResponseEntity<EjemplarDto> crear(@Valid @RequestBody EjemplarDto d) {
        var x = d.toEntity();
        x.setIdEjemplar(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(EjemplarDto.from(service.guardar(x)));
    }

    @PutMapping("/{id}")
    public EjemplarDto actualizar(@PathVariable Integer id, @Valid @RequestBody EjemplarDto d) {
        var x = d.toEntity();
        x.setIdEjemplar(id);
        return EjemplarDto.from(service.guardar(x));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
