package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.UsuarioDtos.Request;
import com.bibliot.gestion_biblioteca.dto.UsuarioDtos.Response;
import com.bibliot.gestion_biblioteca.entities.Usuario;
import com.bibliot.gestion_biblioteca.service.UsuarioService;
import com.bibliot.gestion_biblioteca.service.RolService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/api")
public class UsuarioController {
    private final UsuarioService service;
    private final RolService roles;

    public UsuarioController(UsuarioService service, RolService roles) {
        this.service = service;
        this.roles = roles;
    }

    @GetMapping({ "/admin/usuarios", "/bibliotecario/usuarios" })
    public List<Response> listar() {
        return service.listarTodos().stream().map(Response::desde).toList();
    }

    @GetMapping({ "/admin/usuarios/{id}", "/bibliotecario/usuarios/{id}" })
    public Response buscar(@PathVariable Integer id) {
        return Response.desde(service.buscarPorId(id));
    }

    @GetMapping({ "/admin/usuarios/email/{email}", "/bibliotecario/usuarios/email/{email}" })
    public Response porEmail(@PathVariable String email) {
        return Response.desde(service.buscarPorEmail(email));
    }

    @GetMapping("/admin/usuarios/dni/{dni}")
    public Response porDni(@PathVariable String dni) {
        return Response.desde(service.buscarPorDni(dni));
    }

    @PostMapping({ "/admin/usuarios", "/bibliotecario/usuarios" })
    public ResponseEntity<Response> crear(@Valid @RequestBody Request dto) {
        if (dto.password() == null || dto.password().length() < 8)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña debe tener al menos 8 caracteres");
        Usuario usuario = dto.toEntity();
        usuario.setIdUsuario(null);
        usuario.setRol(roles.buscarPorId(dto.idRol()));
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.desde(service.crearUsuario(usuario)));
    }

    @PutMapping({ "/admin/usuarios/{id}", "/bibliotecario/usuarios/{id}" })
    public Response actualizar(@PathVariable Integer id, @Valid @RequestBody Request dto) {
        Usuario usuario = dto.toEntity();
        usuario.setIdUsuario(id);
        usuario.setRol(roles.buscarPorId(dto.idRol()));
        return Response.desde(service.actualizarUsuario(usuario));
    }

    @DeleteMapping({ "/admin/usuarios/{id}", "/bibliotecario/usuarios/{id}" })
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/lector/perfil/{idUsuario}")
    public Response perfil(@PathVariable Integer idUsuario) {
        return Response.desde(service.buscarPorId(idUsuario));
    }

    @PutMapping("/lector/perfil/{idUsuario}")
    public Response actualizarPerfil(@PathVariable Integer idUsuario, @Valid @RequestBody Request dto) {
        Usuario usuario = dto.toEntity();
        usuario.setIdUsuario(idUsuario);
        usuario.setRol(roles.buscarPorId(dto.idRol()));
        return Response.desde(service.actualizarUsuario(usuario));
    }
}
