package com.bibliot.gestion_biblioteca.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bibliot.gestion_biblioteca.entities.LibroDigital;
import com.bibliot.gestion_biblioteca.repository.LibroDigitalRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class LibroDigitalService {

    @Autowired
    private LibroDigitalRepository libroDigitalRepository;

    public List<LibroDigital> listarTodos() {
        return libroDigitalRepository.findAllConLibro();
    }

    public LibroDigital buscarPorId(Integer id) {
        return libroDigitalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro digital no encontrado con ID: " + id));
    }

    @Transactional
    public LibroDigital guardar(LibroDigital libroDigital) {
        return libroDigitalRepository.save(libroDigital);
    }

    @Transactional
    public LibroDigital actualizar(Integer id, LibroDigital datosRecibidos) {
        LibroDigital existente = buscarPorId(id);

        if (datosRecibidos.getArchivoUrl() != null) {
            existente.setArchivoUrl(datosRecibidos.getArchivoUrl());
        }
        if (datosRecibidos.getFormato() != null) {
            existente.setFormato(datosRecibidos.getFormato());
        }
        if (datosRecibidos.getTamanoArchivo() != null) {
            existente.setTamanoArchivo(datosRecibidos.getTamanoArchivo());
        }
        if (datosRecibidos.getDisponible() != null) {
            existente.setDisponible(datosRecibidos.getDisponible());
        }

        return libroDigitalRepository.save(existente);
    }

    @Transactional
    public void eliminar(Integer id) {
        LibroDigital libroDigital = buscarPorId(id);
        libroDigitalRepository.delete(libroDigital);
    }
}