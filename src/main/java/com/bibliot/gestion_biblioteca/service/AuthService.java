package com.bibliot.gestion_biblioteca.service;

import com.bibliot.gestion_biblioteca.segurity.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bibliot.gestion_biblioteca.entities.Usuario;
import com.bibliot.gestion_biblioteca.repository.UsuarioRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Devuelve el token si las credenciales son correctas, o null si no
    @Transactional(readOnly = true)   // porque Usuario.rol es LAZY
    public String login(String email, String password) {

        Usuario u = usuarioRepository.findByEmail(email).orElse(null);

        if (u != null
                && Boolean.TRUE.equals(u.getActivo())
                && passwordEncoder.matches(password, u.getPassword())) {

            return jwtService.generateToken(u.getEmail(), u.getRol().getNombre().toUpperCase());
        }
        return null;
    }
}