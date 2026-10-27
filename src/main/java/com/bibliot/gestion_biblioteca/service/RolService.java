package com.bibliot.gestion_biblioteca.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bibliot.gestion_biblioteca.entities.Rol;
import com.bibliot.gestion_biblioteca.repository.RolRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }

    public Rol buscarPorId(Integer id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
    }

    @Transactional
    public Rol guardar(Rol rol) {
        // Validar que el nombre no esté repetido
        if (rol.getNombre() != null && !rol.getNombre().isEmpty()) {
            Optional<Rol> existente = rolRepository.findByNombre(rol.getNombre());

            // Si existe Y (es un nuevo rol O el ID no coincide con el existente)
            if (existente.isPresent() && (rol.getIdRol() == null ||
                    !existente.get().getIdRol().equals(rol.getIdRol()))) {
                throw new RuntimeException("El rol '" + rol.getNombre() + "' ya está registrado");
            }
        }
        return rolRepository.save(rol);
    }

    @Transactional
    public Rol actualizar(Integer id, Rol datosActualizados) {
        Rol existente = buscarPorId(id);

        if (datosActualizados.getNombre() != null) {
            existente.setNombre(datosActualizados.getNombre());
        }

        return rolRepository.save(existente);
    }

    @Transactional
    public void eliminar(Integer id) {
        Rol rol = buscarPorId(id);
        rolRepository.delete(rol);
    }

    public Rol buscarPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con nombre: " + nombre));
    }
}
