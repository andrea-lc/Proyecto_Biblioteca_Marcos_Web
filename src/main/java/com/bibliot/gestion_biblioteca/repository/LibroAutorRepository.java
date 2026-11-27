package com.bibliot.gestion_biblioteca.repository;

import com.bibliot.gestion_biblioteca.entities.LibroAutor;
import com.bibliot.gestion_biblioteca.entities.LibroAutorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LibroAutorRepository extends JpaRepository<LibroAutor, LibroAutorId> {
    List<LibroAutor> findByLibroIdLibro(Integer idLibro);

    List<LibroAutor> findByAutorIdAutor(Integer idAutor);

    @Query("SELECT la.autor.idAutor, COUNT(DISTINCT la.libro.idLibro) FROM LibroAutor la GROUP BY la.autor.idAutor")
    List<Object[]> contarLibrosPorAutor();

    @Query("SELECT la.libro.idLibro, CONCAT(CONCAT(la.autor.nombres, ' '), la.autor.apellidos) "
            + "FROM LibroAutor la WHERE la.libro.idLibro IN :libroIds")
    List<Object[]> autoresDeLibros(@Param("libroIds") List<Integer> libroIds);

    void deleteByLibroIdLibro(Integer idLibro);

}
