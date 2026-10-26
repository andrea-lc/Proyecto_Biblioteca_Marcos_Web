package com.bibliot.gestion_biblioteca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bibliot.gestion_biblioteca.entities.Autor;
import com.bibliot.gestion_biblioteca.repository.AutorRepository;
import jakarta.transaction.Transactional;


@Service
public class AutorService {
    @Autowired
    private AutorRepository autorRepository;

    public List<Autor> listarTodos() {
        return autorRepository.findAll();
    }

    public Autor buscarPorId(Integer id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado con ID: " + id));
    }

    @Transactional
    public Autor guardar(Autor autor) {
        return autorRepository.save(autor);
    }

    @Transactional
    public void eliminar(Integer id) {
        Autor autor = buscarPorId(id);
        autorRepository.delete(autor);
    }

    public List<Autor> buscarPorNombre(String nombre) {
        return autorRepository.findByNombresContainingIgnoreCase(nombre);
    }
}