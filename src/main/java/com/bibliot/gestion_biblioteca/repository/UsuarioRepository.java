package com.bibliot.gestion_biblioteca.repository;

import com.bibliot.gestion_biblioteca.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}