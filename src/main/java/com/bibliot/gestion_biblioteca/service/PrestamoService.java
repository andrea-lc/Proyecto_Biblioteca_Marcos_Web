package com.bibliot.gestion_biblioteca.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.bibliot.gestion_biblioteca.entities.Prestamo;
import com.bibliot.gestion_biblioteca.repository.PrestamoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    public List<Prestamo> listarTodos() {
        return prestamoRepository.findAll();
    }

    public Prestamo buscarPorId(Integer id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado con ID: " + id));
    }

    @Transactional
    public Prestamo guardar(Prestamo prestamo) {
        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public Prestamo actualizar(Integer id, Prestamo datosActualizados) {
        Prestamo existente = buscarPorId(id);

        if (datosActualizados.getFechaPrestamo() != null) {
            existente.setFechaPrestamo(datosActualizados.getFechaPrestamo());
        }
        if (datosActualizados.getFechaVencimiento() != null) {
            existente.setFechaVencimiento(datosActualizados.getFechaVencimiento());
        }
        if (datosActualizados.getEstado() != null) {
            existente.setEstado(datosActualizados.getEstado());
        }
        if (datosActualizados.getUsuario() != null) {
            existente.setUsuario(datosActualizados.getUsuario());
        }
        if (datosActualizados.getEjemplar() != null) {
            existente.setEjemplar(datosActualizados.getEjemplar());
        }

        return prestamoRepository.save(existente);
    }

    @Transactional
    public void eliminar(Integer id) {
        Prestamo prestamo = buscarPorId(id);
        prestamoRepository.delete(prestamo);
    }

    public List<Prestamo> buscarPorEstado(String estado) {
        return prestamoRepository.findByEstado(estado);
    }

    public List<Prestamo> buscarPrestamosActivos() {
        return prestamoRepository.findByEstado("ACTIVO");
    }
}