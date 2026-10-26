package com.bibliot.gestion_biblioteca.repository;

import com.bibliot.gestion_biblioteca.entities.Sancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SancionRepository extends JpaRepository<Sancion, Integer> {
    Optional<Sancion> findByDevolucionIdDevolucion(Integer idDevolucion);

    boolean existsByDevolucionIdDevolucion(Integer idDevolucion);

    boolean existsByDevolucionPrestamoUsuarioIdUsuarioAndEstado(Integer idUsuario, String estado);

    List<Sancion> findByDevolucionPrestamoUsuarioIdUsuarioAndEstado(Integer idUsuario, String estado);

    List<Sancion> findByEstado(String estado);

    List<Sancion> findByEstadoAndFechaFinBefore(String estado, LocalDate fecha);
}