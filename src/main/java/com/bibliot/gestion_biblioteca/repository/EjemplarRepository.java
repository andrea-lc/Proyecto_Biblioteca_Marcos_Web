package com.bibliot.gestion_biblioteca.repository;

import com.bibliot.gestion_biblioteca.entities.Ejemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EjemplarRepository extends JpaRepository<Ejemplar, Integer> {
    // Búsqueda exacta por código de inventario (código de barras/etiqueta)
    Optional<Ejemplar> findByCodigoInventario(String codigoInventario);

    // Lista todos los ejemplares físicos de un libro en particular
    List<Ejemplar> findByLibroIdLibro(Integer idLibro);

        @Query("SELECT e FROM Ejemplar e " + "JOIN FETCH e.libro l " + "JOIN FETCH l.editorial")
        List<Ejemplar> findAllConLibro();
    }