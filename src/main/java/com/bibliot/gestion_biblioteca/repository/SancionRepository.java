package com.bibliot.gestion_biblioteca.repository;

import com.bibliot.gestion_biblioteca.entities.Sancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SancionRepository extends JpaRepository<Sancion, Integer> {
    Optional<Sancion> findByDevolucionIdDevolucion(Integer idDevolucion);
}