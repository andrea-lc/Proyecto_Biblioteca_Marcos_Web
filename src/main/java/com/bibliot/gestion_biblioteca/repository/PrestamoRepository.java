package com.bibliot.gestion_biblioteca.repository;

import com.bibliot.gestion_biblioteca.entities.Prestamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {
    // Lista todos los préstamos asociados a un usuario específico
    List<Prestamo> findByUsuarioIdUsuario(Integer idUsuario);

    // Lista los préstamos según su estado (ej. "Activo", "Vencido", "Devuelto")
    List<Prestamo> findByEstado(String estado);

    List<Prestamo> findByEjemplarIdEjemplar(Integer idEjemplar);

    @Query("SELECT p FROM Prestamo p JOIN FETCH p.usuario JOIN FETCH p.ejemplar e JOIN FETCH e.libro "
            + "WHERE UPPER(p.estado) = 'ACTIVO' AND p.fechaVencimiento IS NOT NULL AND p.fechaVencimiento <= :hasta "
            + "AND NOT EXISTS (SELECT d.idDevolucion FROM Devolucion d WHERE d.prestamo = p)")
    List<Prestamo> findActivosPendientesDeVencimiento(@Param("hasta") LocalDateTime hasta);

    @Query("SELECT COUNT(p.idPrestamo) FROM Prestamo p WHERE UPPER(p.estado) IN ('ACTIVO', 'VENCIDO') "
            + "AND NOT EXISTS (SELECT d.idDevolucion FROM Devolucion d WHERE d.prestamo = p)")
    long contarPendientesDashboard();

    @Query("SELECT COUNT(p.idPrestamo) FROM Prestamo p WHERE UPPER(p.estado) IN ('ACTIVO', 'VENCIDO') "
            + "AND NOT EXISTS (SELECT d.idDevolucion FROM Devolucion d WHERE d.prestamo = p) "
            + "AND (UPPER(p.estado) = 'VENCIDO' OR (p.fechaVencimiento IS NOT NULL AND p.fechaVencimiento < :ahora))")
    long contarVencidosDashboard(@Param("ahora") LocalDateTime ahora);

    @Query("SELECT COUNT(p.idPrestamo) FROM Prestamo p WHERE p.fechaPrestamo IS NOT NULL "
            + "AND p.fechaPrestamo >= :desde AND p.fechaPrestamo < :hasta")
    long contarEntreFechas(@Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);

    @Query("SELECT YEAR(p.fechaPrestamo), MONTH(p.fechaPrestamo), COUNT(p.idPrestamo) FROM Prestamo p "
            + "WHERE p.fechaPrestamo IS NOT NULL AND p.fechaPrestamo >= :desde AND p.fechaPrestamo < :hasta "
            + "GROUP BY YEAR(p.fechaPrestamo), MONTH(p.fechaPrestamo)")
    List<Object[]> contarPorMes(@Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);

    @Query("SELECT CONCAT(CONCAT(u.nombres, ' '), u.apellidos), l.titulo, p.fechaPrestamo, p.fechaVencimiento, "
            + "CASE WHEN EXISTS (SELECT d.idDevolucion FROM Devolucion d WHERE d.prestamo = p) THEN 'Devuelto' "
            + "WHEN UPPER(p.estado) = 'POR ENTREGAR' THEN 'Por entregar' "
            + "WHEN UPPER(p.estado) = 'CANCELADO' THEN 'Cancelado' "
            + "WHEN UPPER(p.estado) = 'VENCIDO' OR (p.fechaVencimiento IS NOT NULL AND p.fechaVencimiento < :ahora) THEN 'Vencido' "
            + "ELSE p.estado END FROM Prestamo p JOIN p.usuario u JOIN p.ejemplar e JOIN e.libro l "
            + "ORDER BY COALESCE(p.fechaPrestamo, CURRENT_TIMESTAMP) DESC, p.idPrestamo DESC")
    List<Object[]> ultimosParaDashboard(@Param("ahora") LocalDateTime ahora, Pageable pageable);

    @Query("SELECT l.idLibro, l.titulo, COUNT(p.idPrestamo) FROM Prestamo p JOIN p.ejemplar e JOIN e.libro l "
            + "GROUP BY l.idLibro, l.titulo ORDER BY COUNT(p.idPrestamo) DESC")
    List<Object[]> librosPopularesDashboard(Pageable pageable);

    @Query(value = "SELECT p FROM Prestamo p JOIN FETCH p.usuario JOIN FETCH p.ejemplar e JOIN FETCH e.libro LEFT JOIN FETCH p.reserva "
            + "ORDER BY CASE WHEN p.fechaPrestamo IS NULL THEN 0 ELSE 1 END, p.fechaPrestamo DESC, p.idPrestamo DESC",
            countQuery = "SELECT COUNT(p) FROM Prestamo p")
    Page<Prestamo> findPanelPrestamos(Pageable pageable);

    @Query("SELECT p FROM Prestamo p JOIN FETCH p.usuario JOIN FETCH p.ejemplar e JOIN FETCH e.libro LEFT JOIN FETCH p.reserva WHERE p.idPrestamo = :id")
    java.util.Optional<Prestamo> findPanelById(@Param("id") Integer id);

    @Query("SELECT p FROM Prestamo p JOIN FETCH p.ejemplar e JOIN FETCH e.libro LEFT JOIN FETCH p.reserva WHERE p.reserva.idReserva = :idReserva")
    java.util.Optional<Prestamo> findByReservaIdReserva(@Param("idReserva") Integer idReserva);

    @Modifying
    @Query("UPDATE Prestamo p SET p.estado = :estado WHERE p.idPrestamo = :id")
    int actualizarEstado(@Param("id") Integer id, @Param("estado") String estado);

    @Modifying
    @Query("UPDATE Prestamo p SET p.estado = :estado, p.fechaPrestamo = :fechaPrestamo, p.fechaVencimiento = :fechaVencimiento WHERE p.idPrestamo = :id")
    int actualizarEntrega(@Param("id") Integer id, @Param("estado") String estado,
                          @Param("fechaPrestamo") LocalDateTime fechaPrestamo,
                          @Param("fechaVencimiento") LocalDateTime fechaVencimiento);
}
