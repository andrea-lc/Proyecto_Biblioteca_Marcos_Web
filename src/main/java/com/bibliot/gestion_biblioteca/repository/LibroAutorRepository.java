package com.bibliot.gestion_biblioteca.repository;

import com.bibliot.gestion_biblioteca.entities.LibroAutor;
import com.bibliot.gestion_biblioteca.entities.LibroAutorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LibroAutorRepository extends JpaRepository<LibroAutor, LibroAutorId> {
    List<LibroAutor> findByLibroIdLibro(Integer idLibro);
    List<LibroAutor> findByAutorIdAutor(Integer idAutor);
}