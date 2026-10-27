package com.bibliot.gestion_biblioteca.repository;

import com.bibliot.gestion_biblioteca.entities.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Integer> {

    // Filtro básico por título usando JPQL derivado
    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    // Filtro dinámico por año extrayendo la función de fecha
    @Query("SELECT l FROM Libro l WHERE YEAR(l.fechaPublicacion) = :anio")
    List<Libro> findByAnioPublicacion(@Param("anio") int anio);

    boolean existsByIsbn(String isbn);

    List<Libro> findByEditorialIdEditorial(Integer idEditorial);

    List<Libro> findByIsbn(String isbn);

    @Query("SELECT l FROM Libro l JOIN FETCH l.editorial")
    List<Libro> findAllConEditorial();
    /*
     * Búsqueda full-text delegada a MySQL (Requiere índice FULLTEXT en la BD)
     * 
     * @Query(value =
     * "SELECT * FROM libro WHERE MATCH(titulo, descripcion, contenido_texto) AGAINST(:keyword IN BOOLEAN MODE)"
     * , nativeQuery = true)
     * List<Libro> searchFullText(@Param("keyword") String keyword);
     */
}