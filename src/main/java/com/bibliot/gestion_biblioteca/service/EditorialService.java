package com.bibliot.gestion_biblioteca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bibliot.gestion_biblioteca.entities.Editorial;
import com.bibliot.gestion_biblioteca.entities.Libro;
import com.bibliot.gestion_biblioteca.repository.EditorialRepository;
import com.bibliot.gestion_biblioteca.repository.LibroRepository;
import jakarta.transaction.Transactional;

@Service
public class EditorialService {
    @Autowired
    private EditorialRepository editorialRepository;

    @Autowired
    private LibroRepository libros;

    public List<Editorial> listarTodos() {
        return editorialRepository.findAll();
    }

    public Editorial buscarPorId(Integer id) {
        return editorialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Editorial no encontrada con ID: " + id));
    }

    @Transactional
    public Editorial guardar(Editorial editorial) {
        return editorialRepository.save(editorial);
    }

    @Transactional
    public Editorial actualizar(Integer id, Editorial datosActualizados) {
        Editorial existente = buscarPorId(id);

        if (datosActualizados.getNombre() != null) {
            existente.setNombre(datosActualizados.getNombre());
        }
        if (datosActualizados.getPais() != null) {
            existente.setPais(datosActualizados.getPais());
        }
        if (datosActualizados.getSitioWeb() != null) {
            existente.setSitioWeb(datosActualizados.getSitioWeb());
        }

        return editorialRepository.save(existente);
    }

    @Transactional
    public void eliminar(Integer id) {
        Editorial editorial = buscarPorId(id);
        List<Libro> asociados = libros.findByEditorialIdEditorial(id);
        asociados.forEach(libro -> libro.setEditorial(null));
        libros.saveAll(asociados);
        editorialRepository.delete(editorial);
    }
}
