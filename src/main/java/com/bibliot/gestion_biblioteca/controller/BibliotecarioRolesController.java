package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.RolDtos.Response;
import com.bibliot.gestion_biblioteca.service.RolService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Consulta de roles para vistas de solo lectura del personal bibliotecario. */
@RestController
public class BibliotecarioRolesController {
    private final RolService service;

    public BibliotecarioRolesController(RolService service) {
        this.service = service;
    }

    @GetMapping("/api/bibliotecario/roles")
    public List<Response> listar() {
        return service.listarTodos().stream().map(Response::desde).toList();
    }
}
