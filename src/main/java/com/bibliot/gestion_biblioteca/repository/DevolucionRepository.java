package com.bibliot.gestion_biblioteca.repository;

import com.bibliot.gestion_biblioteca.entities.Devolucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DevolucionRepository extends JpaRepository<Devolucion, Integer> {
    // Encuentra la devolución asociada a un préstamo específico
    Optional<Devolucion> findByPrestamoIdPrestamo(Integer idPrestamo);

    List<Devolucion> findByPrestamoUsuarioIdUsuario(Integer idUsuario);
}