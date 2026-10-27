package com.bibliot.gestion_biblioteca.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bibliot.gestion_biblioteca.entities.Ejemplar;
import com.bibliot.gestion_biblioteca.repository.EjemplarRepository;
import jakarta.transaction.Transactional;

@Service
public class EjemplarService {
    @Autowired
    private EjemplarRepository ejemplarRepository;

    public List<Ejemplar> listarTodos() {
        return ejemplarRepository.findAllConLibro();
    }

    public Ejemplar buscarPorId(Integer id) {
        return ejemplarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ejemplar no encontrado con ID: " + id));
    }

    @Transactional
    public Ejemplar guardar(Ejemplar ejemplar) {
        // Validar código de inventario único
        if (ejemplar.getCodigoInventario() != null && !ejemplar.getCodigoInventario().isEmpty()) {
            Optional<Ejemplar> existente = ejemplarRepository.findByCodigoInventario(ejemplar.getCodigoInventario());

            // Si existe Y (es un nuevo ejemplar O el ID no coincide)
            if (existente.isPresent() && (ejemplar.getIdEjemplar() == null ||
                    !existente.get().getIdEjemplar().equals(ejemplar.getIdEjemplar()))) {
                throw new RuntimeException("El código de inventario ya está registrado");
            }
        }
        return ejemplarRepository.save(ejemplar);
    }

    @Transactional
    public Ejemplar actualizar(Integer id, Ejemplar datosActualizados) {
        Ejemplar existente = buscarPorId(id);

        if (datosActualizados.getCodigoInventario() != null) {
            existente.setCodigoInventario(datosActualizados.getCodigoInventario());
        }
        if (datosActualizados.getUbicacion() != null) {
            existente.setUbicacion(datosActualizados.getUbicacion());
        }
        if (datosActualizados.getEstado() != null) {
            existente.setEstado(datosActualizados.getEstado());
        }
        if (datosActualizados.getFechaAdquisicion() != null) {
            existente.setFechaAdquisicion(datosActualizados.getFechaAdquisicion());
        }

        return ejemplarRepository.save(existente);
    }

    @Transactional
    public void eliminar(Integer id) {
        Ejemplar ejemplar = buscarPorId(id);
        ejemplarRepository.delete(ejemplar);
    }

    public Ejemplar buscarPorCodigoInventario(String codigo) {
        return ejemplarRepository.findByCodigoInventario(codigo)
                .orElseThrow(() -> new RuntimeException("Ejemplar no encontrado con código: " + codigo));
    }
}