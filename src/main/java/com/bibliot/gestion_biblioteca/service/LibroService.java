package com.bibliot.gestion_biblioteca.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.bibliot.gestion_biblioteca.entities.Libro;
import com.bibliot.gestion_biblioteca.repository.LibroRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    public List<Libro> listarTodos() {
        return libroRepository.findAll();
    }

    public Libro buscarPorId(Integer id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + id));
    }

    @Transactional
    public Libro guardar(Libro libro) {
        // Validar ISBN si existe
        if (libro.getIsbn() != null && !libro.getIsbn().isEmpty()) {
            List<Libro> existentes = libroRepository.findByIsbn(libro.getIsbn());
            if (!existentes.isEmpty() && (libro.getIdLibro() == null ||
                    !existentes.get(0).getIdLibro().equals(libro.getIdLibro()))) {
                throw new RuntimeException("El ISBN ya está registrado");
            }
        }
        return libroRepository.save(libro);
    }

    @Transactional
    public void eliminar(Integer id) {
        Libro libro = buscarPorId(id);
        libroRepository.delete(libro);
    }

    public List<Libro> buscarPorTitulo(String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Libro> buscarPorEditorial(Integer idEditorial) {
        return libroRepository.findByEditorialIdEditorial(idEditorial);
    }
}