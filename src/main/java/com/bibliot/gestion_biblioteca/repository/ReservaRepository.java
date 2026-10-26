package com.bibliot.gestion_biblioteca.repository;

import com.bibliot.gestion_biblioteca.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    List<Reserva> findByUsuarioIdUsuario(Integer idUsuario);

    List<Reserva> findByLibroIdLibro(Integer idLibro);

    boolean existsByUsuarioIdUsuarioAndLibroIdLibroAndEstado(Integer idUsuario, Integer idLibro, String estado);

    List<Reserva> findByUsuarioIdUsuarioAndEstado(Integer idUsuario, String estado);

    List<Reserva> findByLibroIdLibroAndEstadoOrderByFechaReserva(Integer idLibro, String estado);

    List<Reserva> findByEstadoAndFechaExpiracionBefore(String estado, LocalDateTime fecha);

    long countByLibroIdLibroAndFechaReservaBeforeAndEstado(Integer idLibro, LocalDateTime fecha, String estado);
}