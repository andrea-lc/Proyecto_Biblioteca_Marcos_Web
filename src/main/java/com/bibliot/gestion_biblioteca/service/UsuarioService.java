package com.bibliot.gestion_biblioteca.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bibliot.gestion_biblioteca.entities.Usuario;
import com.bibliot.gestion_biblioteca.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Transactional
    public Usuario crearUsuario(Usuario usuario) {
        // Validar que no exista
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (usuarioRepository.findByDni(usuario.getDni()).isPresent()) {
            throw new RuntimeException("El DNI ya está registrado");
        }

        // Encriptar contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actualizar(Integer id, Usuario datosActualizados) {
        Usuario existente = buscarPorId(id);

        if (datosActualizados.getNombres() != null) {
            existente.setNombres(datosActualizados.getNombres());
        }
        if (datosActualizados.getApellidos() != null) {
            existente.setApellidos(datosActualizados.getApellidos());
        }
        if (datosActualizados.getDni() != null) {
            existente.setDni(datosActualizados.getDni());
        }
        if (datosActualizados.getEmail() != null) {
            existente.setEmail(datosActualizados.getEmail());
        }
        if (datosActualizados.getTelefono() != null) {
            existente.setTelefono(datosActualizados.getTelefono());
        }
        if (datosActualizados.getDireccion() != null) {
            existente.setDireccion(datosActualizados.getDireccion());
        }
        if (datosActualizados.getPassword() != null && !datosActualizados.getPassword().isEmpty()) {
            existente.setPassword(passwordEncoder.encode(datosActualizados.getPassword()));
        }
        if (datosActualizados.getRol() != null) {
            existente.setRol(datosActualizados.getRol());
        }
        if (datosActualizados.getActivo() != null) {
            existente.setActivo(datosActualizados.getActivo());
        }
        return usuarioRepository.save(existente);
    }

    @Transactional
    public void eliminar(Integer id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    public Usuario buscarPorDni(String dni) {
        return usuarioRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con DNI: " + dni));
    }
}