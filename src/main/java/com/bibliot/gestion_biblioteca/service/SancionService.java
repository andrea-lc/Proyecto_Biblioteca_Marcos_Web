package com.bibliot.gestion_biblioteca.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.bibliot.gestion_biblioteca.entities.Sancion;
import com.bibliot.gestion_biblioteca.repository.SancionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class SancionService {

    @Autowired
    private SancionRepository sancionRepository;

    public List<Sancion> listarTodos() {
        return sancionRepository.findAll();
    }

    public Sancion buscarPorId(Integer id) {
        return sancionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sanción no encontrada con ID: " + id));
    }

    @Transactional
    public Sancion guardar(Sancion sancion) {
        return sancionRepository.save(sancion);
    }

    @Transactional
    public void eliminar(Integer id) {
        Sancion sancion = buscarPorId(id);
        sancionRepository.delete(sancion);
    }

    public List<Sancion> buscarPorEstado(String estado) {
        return sancionRepository.findByEstado(estado);
    }
}