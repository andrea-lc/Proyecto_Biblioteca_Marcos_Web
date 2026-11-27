package com.bibliot.gestion_biblioteca.repository;

import com.bibliot.gestion_biblioteca.entities.LibroCategoria;
import com.bibliot.gestion_biblioteca.entities.LibroCategoriaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LibroCategoriaRepository extends JpaRepository<LibroCategoria, LibroCategoriaId> {
    List<LibroCategoria> findByLibroIdLibro(Integer idLibro);

    List<LibroCategoria> findByCategoriaIdCategoria(Integer idCategoria);

    void deleteByLibroIdLibro(Integer idLibro);

    @Query("SELECT lc.categoria.nombre, COUNT(DISTINCT p.idPrestamo) FROM Prestamo p "
            + "JOIN p.ejemplar e JOIN e.libro l, LibroCategoria lc "
            + "WHERE lc.libro.idLibro = l.idLibro "
            + "GROUP BY lc.categoria.idCategoria, lc.categoria.nombre "
            + "ORDER BY COUNT(DISTINCT p.idPrestamo) DESC")
    List<Object[]> categoriasPopularesDashboard(Pageable pageable);

    @Query("SELECT lc.libro.idLibro, lc.categoria.nombre FROM LibroCategoria lc WHERE lc.libro.idLibro IN :libroIds ORDER BY lc.categoria.nombre")
    List<Object[]> categoriasDeLibros(@Param("libroIds") List<Integer> libroIds);

}
