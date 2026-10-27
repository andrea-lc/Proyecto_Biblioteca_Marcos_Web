package com.bibliot.gestion_biblioteca.repository;

import com.bibliot.gestion_biblioteca.entities.Ejemplar;
import com.bibliot.gestion_biblioteca.entities.LibroDigital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroDigitalRepository extends JpaRepository<LibroDigital, Integer> {
    Optional<LibroDigital> findByLibroIdLibro(Integer idLibro);

    List<LibroDigital> findByDisponible(Boolean disponible);

    @Query("SELECT DISTINCT ld FROM LibroDigital ld " +
            "JOIN FETCH ld.libro l " +
            "JOIN FETCH l.editorial ")
    List<LibroDigital> findAllConLibro();
}