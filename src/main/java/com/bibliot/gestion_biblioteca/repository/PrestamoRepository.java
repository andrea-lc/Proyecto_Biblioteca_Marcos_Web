package com.bibliot.gestion_biblioteca.repository;

import com.bibliot.gestion_biblioteca.entities.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {

    // Lista todos los préstamos asociados a un usuario específico
    List<Prestamo> findByUsuarioIdUsuario(Integer idUsuario);

    // Lista los préstamos según su estado (ej. "Activo", "Vencido", "Devuelto")
    List<Prestamo> findByEstado(String estado);
}