package com.bibliot.gestion_biblioteca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bibliot.gestion_biblioteca.entities.Editorial;
import com.bibliot.gestion_biblioteca.repository.EditorialRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.transaction.Transactional;


@Service    
public class EditorialService {
    @Autowired
    private EditorialRepository editorialRepository;

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
    public void eliminar(Integer id) {
        Editorial editorial = buscarPorId(id);
        editorialRepository.delete(editorial);
    }
}