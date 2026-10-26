package com.bibliot.gestion_biblioteca.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.bibliot.gestion_biblioteca.entities.Devolucion;
import com.bibliot.gestion_biblioteca.repository.DevolucionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class DevolucionService {
    @Autowired
    private DevolucionRepository devolucionRepository;

    public List<Devolucion> listarTodos() {
        return devolucionRepository.findAll();
    }

    public Devolucion buscarPorId(Integer id) {
        return devolucionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Devolución no encontrada con ID: " + id));
    }

    @Transactional
    public Devolucion guardar(Devolucion devolucion) {
        return devolucionRepository.save(devolucion);
    }

    @Transactional
    public void eliminar(Integer id) {
        Devolucion devolucion = buscarPorId(id);
        devolucionRepository.delete(devolucion);
    }

}