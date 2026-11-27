package com.bibliot.gestion_biblioteca.repository;

import com.bibliot.gestion_biblioteca.entities.Ejemplar;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EjemplarRepository extends JpaRepository<Ejemplar, Integer> {
    // Búsqueda exacta por código de inventario (código de barras/etiqueta)
    Optional<Ejemplar> findByCodigoInventario(String codigoInventario);

    // Lista todos los ejemplares físicos de un libro en particular
    List<Ejemplar> findByLibroIdLibro(Integer idLibro);

    @Query("SELECT COUNT(e) FROM Ejemplar e WHERE e.libro.idLibro = :idLibro AND UPPER(TRIM(e.estado)) = 'DISPONIBLE'")
    int countDisponiblesByLibroId(@Param("idLibro") Integer idLibro);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Ejemplar e WHERE e.libro.idLibro = :idLibro AND UPPER(TRIM(e.estado)) = 'DISPONIBLE' ORDER BY e.idEjemplar ASC")
    List<Ejemplar> findDisponiblesParaApartar(@Param("idLibro") Integer idLibro, Pageable pageable);

    @Query("SELECT e FROM Ejemplar e " + "JOIN FETCH e.libro l " + "JOIN FETCH l.editorial")
    List<Ejemplar> findAllConLibro();
}