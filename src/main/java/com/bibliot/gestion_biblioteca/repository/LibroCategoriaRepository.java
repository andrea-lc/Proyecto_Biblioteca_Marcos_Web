package com.bibliot.gestion_biblioteca.repository;

import com.bibliot.gestion_biblioteca.entities.LibroCategoria;
import com.bibliot.gestion_biblioteca.entities.LibroCategoriaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LibroCategoriaRepository extends JpaRepository<LibroCategoria, LibroCategoriaId> {
    List<LibroCategoria> findByLibroIdLibro(Integer idLibro);
    List<LibroCategoria> findByCategoriaIdCategoria(Integer idCategoria);
}