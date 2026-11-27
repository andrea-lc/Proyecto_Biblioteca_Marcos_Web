package com.bibliot.gestion_biblioteca.dto;

import com.bibliot.gestion_biblioteca.entities.Rol;
import com.bibliot.gestion_biblioteca.entities.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public final class UsuarioDtos {
    private UsuarioDtos() {}

    public record Request(Integer idUsuario, @NotBlank @Size(max = 100) String nombres,
            @NotBlank @Size(max = 100) String apellidos,
            @NotBlank @Pattern(regexp = "^[0-9]{8}$") String dni,
            @NotBlank @Email @Size(max = 150) String email,
            @NotBlank @Pattern(regexp = "^[0-9]{9}$") String telefono,
            @Size(max = 200) String direccion, @NotNull @Positive Integer idRol,
            @Size(max = 255) String password, @PastOrPresent LocalDateTime fechaRegistro,
            Boolean activo) {
        public Usuario toEntity() {
            Usuario x = new Usuario(); x.setIdUsuario(idUsuario); x.setNombres(nombres);
            x.setApellidos(apellidos); x.setDni(dni); x.setEmail(email); x.setTelefono(telefono);
            x.setDireccion(direccion); Rol rol = new Rol(); rol.setIdRol(idRol); x.setRol(rol);
            x.setPassword(password); x.setFechaRegistro(fechaRegistro);
            if (activo != null) x.setActivo(activo);
            return x;
        }
    }

    public record Response(Integer idUsuario, String nombres, String apellidos, String dni,
            String email, String telefono, String direccion, Integer idRol, String rol,
            LocalDateTime fechaRegistro, Boolean activo) {
        public static Response desde(Usuario x) {
            return new Response(x.getIdUsuario(), x.getNombres(), x.getApellidos(), x.getDni(),
                    x.getEmail(), x.getTelefono(), x.getDireccion(),
                    x.getRol() == null ? null : x.getRol().getIdRol(),
                    x.getRol() == null ? null : x.getRol().getNombre(), x.getFechaRegistro(), x.getActivo());
        }
    }
}
