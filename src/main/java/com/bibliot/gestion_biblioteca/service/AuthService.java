package com.bibliot.gestion_biblioteca.service;

import com.bibliot.gestion_biblioteca.dto.RegistroDtos;
import com.bibliot.gestion_biblioteca.entities.Rol;
import com.bibliot.gestion_biblioteca.entities.Usuario;
import com.bibliot.gestion_biblioteca.repository.RolRepository;
import com.bibliot.gestion_biblioteca.repository.UsuarioRepository;
import com.bibliot.gestion_biblioteca.segurity.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public static String normalizarRol(String nombre) {
        return nombre == null
                ? ""
                : nombre.trim().toUpperCase(java.util.Locale.ROOT);
    }

    // Devuelve el token si las credenciales son correctas, o null si no
    @Transactional(readOnly = true)
    public String login(String email, String password) {
        Usuario u = usuarioRepository.findByEmail(email).orElse(null);

        if (u != null
                && Boolean.TRUE.equals(u.getActivo())
                && passwordEncoder.matches(password, u.getPassword())) {

            String rol = normalizarRol(u.getRol().getNombre());
            String nombre = u.getNombres() + " " + u.getApellidos();
            return jwtService.generateToken(u.getEmail(), rol, u.getIdUsuario(), nombre);
        }
        return null;
    }

    // Crea un usuario con rol Lector. Lanza IllegalArgumentException con un mensaje legible si algo está mal
    @Transactional
    public Usuario registrar(RegistroDtos r) {

        // "Nombres y apellidos" viene en un solo campo: 2 palabras = 1 nombre + 1 apellido;
        // 3 o más = las dos últimas son los apellidos
        String completo = r.getNombres() == null ? "" : r.getNombres().trim().replaceAll("\\s+", " ");
        String[] t = completo.isEmpty() ? new String[0] : completo.split(" ");
        if (t.length < 2) {
            throw new IllegalArgumentException("Ingresa tus nombres y apellidos.");
        }
        int corte = t.length == 2 ? 1 : t.length - 2;
        String nombres = String.join(" ", Arrays.copyOfRange(t, 0, corte));
        String apellidos = String.join(" ", Arrays.copyOfRange(t, corte, t.length));

        String dni = r.getDni() == null ? "" : r.getDni().trim();
        if (!dni.matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos.");
        }

        String correo = r.getCorreo() == null ? "" : r.getCorreo().trim();
        if (!correo.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("El correo electrónico no es válido.");
        }

        String telefono = r.getTelefono() == null ? "" : r.getTelefono().trim();
        if (!telefono.matches("\\d{9}")) {
            throw new IllegalArgumentException("El teléfono debe tener exactamente 9 dígitos.");
        }

        String password = r.getPassword();
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
        }
        if (!password.equals(r.getConfirmarPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }

        String direccion = r.getDireccion() == null ? null : r.getDireccion().trim();
        if (nombres.length() > 100 || apellidos.length() > 100 || correo.length() > 150
                || (direccion != null && direccion.length() > 200)) {
            throw new IllegalArgumentException("Alguno de los datos es demasiado largo.");
        }

        Rol lector = rolRepository.findByNombre("Lector")
                .orElseThrow(() -> new IllegalStateException("No existe el rol Lector"));

        Usuario u = new Usuario();
        u.setNombres(nombres);
        u.setApellidos(apellidos);
        u.setDni(dni);
        u.setEmail(correo);
        u.setTelefono(telefono);
        u.setDireccion(direccion == null || direccion.isEmpty() ? null : direccion);
        u.setPassword(password);
        u.setRol(lector);
        u.setActivo(true);
        u.setFechaRegistro(LocalDateTime.now().minusSeconds(1));

        // crearUsuario valida email/DNI repetidos y encripta la contraseña
        return usuarioService.crearUsuario(u);
    }
}