package com.bibliot.gestion_biblioteca.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bibliot.gestion_biblioteca.entities.Usuario;
import com.bibliot.gestion_biblioteca.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService{
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
    public Usuario actualizarUsuario(Usuario usuario) {
        // Primero buscamos el existente
        Usuario existente = buscarPorId(usuario.getIdUsuario());

        // Validar email solo si cambió
        if (!existente.getEmail().equals(usuario.getEmail())) {
            if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
                throw new RuntimeException("El email ya está registrado por otro usuario");
            }
        }

        // Validar DNI solo si cambió
        if (!existente.getDni().equals(usuario.getDni())) {
            if (usuarioRepository.findByDni(usuario.getDni()).isPresent()) {
                throw new RuntimeException("El DNI ya está registrado por otro usuario");
            }
        }

        // Actualizar campos
        existente.setNombres(usuario.getNombres());
        existente.setApellidos(usuario.getApellidos());
        existente.setEmail(usuario.getEmail());
        existente.setDni(usuario.getDni());
        existente.setTelefono(usuario.getTelefono());
        existente.setDireccion(usuario.getDireccion());
        if (usuario.getRol() != null) {
            existente.setRol(usuario.getRol());
        }
        if (usuario.getActivo() != null) {
            existente.setActivo(usuario.getActivo());
        }

        // Actualizar contraseña solo si se proporcionó una nueva
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            existente.setPassword(passwordEncoder.encode(usuario.getPassword()));
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
