package com.bibliot.gestion_biblioteca.repository;

import com.bibliot.gestion_biblioteca.entities.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    List<Notificacion> findByUsuarioIdUsuarioOrderByFechaCreacionDesc(Integer idUsuario);
    long countByUsuarioIdUsuarioAndFechaLecturaIsNull(Integer idUsuario);
    boolean existsByEventKeyAndUsuarioIdUsuario(String eventKey, Integer idUsuario);

    @Query("SELECT n.eventKey, n.usuario.idUsuario FROM Notificacion n WHERE n.eventKey IN :eventKeys")
    List<Object[]> findExistingEvents(@Param("eventKeys") List<String> eventKeys);
}
