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

    public List<Prestamo> buscarPorUsuario(Integer idUsuario) {
        return prestamoRepository.findByUsuarioIdUsuario(idUsuario);
    }
}
