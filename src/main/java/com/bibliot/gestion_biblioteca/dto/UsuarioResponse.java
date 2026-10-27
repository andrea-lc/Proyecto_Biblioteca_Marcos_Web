package com.bibliot.gestion_biblioteca.dto;

import com.bibliot.gestion_biblioteca.entities.Usuario;
import java.time.LocalDateTime;

/** Respuesta pública que nunca incluye la contraseña. */
public record UsuarioResponse(Integer idUsuario, String nombres, String apellidos, String dni,
        String email, String telefono, String direccion, Integer idRol, String rol,
        LocalDateTime fechaRegistro, Boolean activo) {
    public static UsuarioResponse desde(Usuario usuario) {
        return new UsuarioResponse(usuario.getIdUsuario(), usuario.getNombres(), usuario.getApellidos(),
                usuario.getDni(), usuario.getEmail(), usuario.getTelefono(), usuario.getDireccion(),
                usuario.getRol() == null ? null : usuario.getRol().getIdRol(),
                usuario.getRol() == null ? null : usuario.getRol().getNombre(),
                usuario.getFechaRegistro(), usuario.getActivo());
    }
}
