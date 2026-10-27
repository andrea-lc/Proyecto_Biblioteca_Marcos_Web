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
        return libroDigitalRepository.findAll();
    }

    public LibroDigital buscarPorId(Integer id) {
        return libroDigitalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro digital no encontrado con ID: " + id));
    }

    public LibroDigital buscarPorLibro(Integer idLibro) {
        return libroDigitalRepository.findByLibroIdLibro(idLibro)
                .orElseThrow(() -> new RuntimeException("Libro digital no encontrado para el libro: " + idLibro));
    }

    @Transactional
    public LibroDigital guardar(LibroDigital libroDigital) {
        return libroDigitalRepository.save(libroDigital);
    }

    @Transactional
    public void eliminar(Integer id) {
        LibroDigital libroDigital = buscarPorId(id);
        libroDigitalRepository.delete(libroDigital);
    }
}
