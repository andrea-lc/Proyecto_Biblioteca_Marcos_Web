package com.bibliot.gestion_biblioteca.repository;

import com.bibliot.gestion_biblioteca.entities.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    List<Reserva> findByUsuarioIdUsuario(Integer idUsuario);

    List<Reserva> findByLibroIdLibro(Integer idLibro);

    boolean existsByUsuarioIdUsuarioAndLibroIdLibroAndEstado(Integer idUsuario, Integer idLibro, String estado);

    List<Reserva> findByUsuarioIdUsuarioAndEstado(Integer idUsuario, String estado);

    List<Reserva> findByLibroIdLibroAndEstadoOrderByFechaReserva(Integer idLibro, String estado);

    List<Reserva> findByEstadoAndFechaExpiracionBefore(String estado, LocalDateTime fecha);

    long countByLibroIdLibroAndFechaReservaBeforeAndEstado(Integer idLibro, LocalDateTime fecha, String estado);

    @Query(value = "SELECT r FROM Reserva r JOIN FETCH r.usuario JOIN FETCH r.libro ORDER BY r.fechaReserva DESC, r.idReserva DESC",
            countQuery = "SELECT COUNT(r) FROM Reserva r")
    Page<Reserva> findPanelReservas(Pageable pageable);

    @Query("SELECT r FROM Reserva r JOIN FETCH r.usuario JOIN FETCH r.libro WHERE r.idReserva = :id")
    Optional<Reserva> findPanelById(@Param("id") Integer id);

    @Query("SELECT r FROM Reserva r JOIN FETCH r.usuario JOIN FETCH r.libro WHERE UPPER(r.estado) = 'CONFIRMADA' AND r.fechaExpiracion < :ahora")
    List<Reserva> findConfirmadasExpiradas(@Param("ahora") LocalDateTime ahora);

    @Modifying
    @Query("UPDATE Reserva r SET r.estado = :estado WHERE r.idReserva = :id")
    int actualizarEstado(@Param("id") Integer id, @Param("estado") String estado);
}