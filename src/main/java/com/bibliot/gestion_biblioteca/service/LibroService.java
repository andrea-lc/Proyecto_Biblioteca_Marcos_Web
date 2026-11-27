package com.bibliot.gestion_biblioteca.service;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.search.engine.search.common.BooleanOperator;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bibliot.gestion_biblioteca.entities.Libro;
import com.bibliot.gestion_biblioteca.repository.LibroRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Libro> listarTodos() {
        return libroRepository.findAllConEditorial();
    }

    public Page<Libro> listarTodos(Pageable pageable) {
        return libroRepository.findAllConEditorial(pageable);
    }

    public Page<Libro> buscarPagina(String titulo, Integer autorId, Integer editorialId,
            Integer categoriaId, String estado, String formato, Pageable pageable) {
        String termino = titulo == null || titulo.isBlank() ? null : titulo.trim();
        String estadoFiltro = estado == null || estado.isBlank() ? null : estado.trim();
        String formatoFiltro = formato == null || formato.isBlank() ? null : formato.trim().toUpperCase(java.util.Locale.ROOT);
        return libroRepository.buscarPagina(termino, autorId, editorialId, categoriaId,
                estadoFiltro, formatoFiltro, pageable);
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
    public Libro actualizar(Integer id, Libro datosActualizados) {
        Libro existente = buscarPorId(id);

        if (datosActualizados.getTitulo() != null) {
            existente.setTitulo(datosActualizados.getTitulo());
        }
        if (datosActualizados.getPaginas() != null) {
            existente.setPaginas(datosActualizados.getPaginas());
        }
        if (datosActualizados.getDescripcion() != null) {
            existente.setDescripcion(datosActualizados.getDescripcion());
        }
        if (datosActualizados.getContenidoTexto() != null) {
            existente.setContenidoTexto(datosActualizados.getContenidoTexto());
        }
        if (datosActualizados.getFechaPublicacion() != null) {
            existente.setFechaPublicacion(datosActualizados.getFechaPublicacion());
        }
        if (datosActualizados.getIsbn() != null) {
            existente.setIsbn(datosActualizados.getIsbn());
        }
        if (datosActualizados.getEstado() != null) {
            existente.setEstado(datosActualizados.getEstado());
        }
        if (datosActualizados.getEditorial() != null) {
            existente.setEditorial(datosActualizados.getEditorial());
        }

        return libroRepository.save(existente);
    }

    @Transactional
    public void eliminar(Integer id) {
        Libro libro = buscarPorId(id);
        libroRepository.delete(libro);
    }

    public List<Libro> buscarPorTitulo(String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public Page<Libro> buscarPorTitulo(String titulo, Pageable pageable) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo, pageable);
    }

    public List<Libro> buscarTextoCompleto(String consulta) {
        if (consulta == null || consulta.isBlank()) return List.of();
        return Search.session(entityManager).search(Libro.class)
                .where(f -> f.simpleQueryString()
                        .fields("titulo", "descripcion", "contenidoTexto", "textoExtraido")
                        .matching(consulta)
                        .defaultOperator(BooleanOperator.OR))
                .fetchHits(100);
    }

    public List<Libro> buscarPorEditorial(Integer idEditorial) {
        return libroRepository.findByEditorialIdEditorial(idEditorial);
    }
}
