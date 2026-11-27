package com.bibliot.gestion_biblioteca.repository;

import com.bibliot.gestion_biblioteca.entities.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Integer> {

    // Filtro básico por título usando JPQL derivado
    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    Page<Libro> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);

    @Query(value = "SELECT l FROM Libro l LEFT JOIN FETCH l.editorial "
            + "WHERE (:titulo IS NULL OR LOWER(l.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))) "
            + "AND (:autorId IS NULL OR EXISTS (SELECT la.id.idLibro FROM LibroAutor la "
            + "WHERE la.libro = l AND la.autor.idAutor = :autorId)) "
            + "AND (:editorialId IS NULL OR l.editorial.idEditorial = :editorialId) "
            + "AND (:categoriaId IS NULL OR EXISTS (SELECT lc.id.idLibro FROM LibroCategoria lc "
            + "WHERE lc.libro = l AND lc.categoria.idCategoria = :categoriaId)) "
            + "AND (:estado IS NULL OR LOWER(l.estado) = LOWER(:estado)) "
            + "AND (:formato IS NULL "
            + "OR (:formato = 'FISICO' AND EXISTS (SELECT e.idEjemplar FROM Ejemplar e WHERE e.libro = l)) "
            + "OR (:formato = 'DIGITAL' AND EXISTS (SELECT ld.idLibroDigital FROM LibroDigital ld WHERE ld.libro = l)) "
            + "OR (:formato = 'AMBOS' AND EXISTS (SELECT e.idEjemplar FROM Ejemplar e WHERE e.libro = l) "
            + "AND EXISTS (SELECT ld.idLibroDigital FROM LibroDigital ld WHERE ld.libro = l)) "
            + "OR (:formato = 'SIN_FORMATO' AND NOT EXISTS (SELECT e.idEjemplar FROM Ejemplar e WHERE e.libro = l) "
            + "AND NOT EXISTS (SELECT ld.idLibroDigital FROM LibroDigital ld WHERE ld.libro = l)))",
            countQuery = "SELECT COUNT(l) FROM Libro l "
                    + "WHERE (:titulo IS NULL OR LOWER(l.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))) "
                    + "AND (:autorId IS NULL OR EXISTS (SELECT la.id.idLibro FROM LibroAutor la "
                    + "WHERE la.libro = l AND la.autor.idAutor = :autorId)) "
                    + "AND (:editorialId IS NULL OR l.editorial.idEditorial = :editorialId) "
                    + "AND (:categoriaId IS NULL OR EXISTS (SELECT lc.id.idLibro FROM LibroCategoria lc "
                    + "WHERE lc.libro = l AND lc.categoria.idCategoria = :categoriaId)) "
                    + "AND (:estado IS NULL OR LOWER(l.estado) = LOWER(:estado)) "
                    + "AND (:formato IS NULL "
                    + "OR (:formato = 'FISICO' AND EXISTS (SELECT e.idEjemplar FROM Ejemplar e WHERE e.libro = l)) "
                    + "OR (:formato = 'DIGITAL' AND EXISTS (SELECT ld.idLibroDigital FROM LibroDigital ld WHERE ld.libro = l)) "
                    + "OR (:formato = 'AMBOS' AND EXISTS (SELECT e.idEjemplar FROM Ejemplar e WHERE e.libro = l) "
                    + "AND EXISTS (SELECT ld.idLibroDigital FROM LibroDigital ld WHERE ld.libro = l)) "
                    + "OR (:formato = 'SIN_FORMATO' AND NOT EXISTS (SELECT e.idEjemplar FROM Ejemplar e WHERE e.libro = l) "
                    + "AND NOT EXISTS (SELECT ld.idLibroDigital FROM LibroDigital ld WHERE ld.libro = l)))")
    Page<Libro> buscarPagina(@Param("titulo") String titulo, @Param("autorId") Integer autorId,
            @Param("editorialId") Integer editorialId, @Param("categoriaId") Integer categoriaId,
            @Param("estado") String estado, @Param("formato") String formato, Pageable pageable);

    // Filtro dinámico por año extrayendo la función de fecha
    @Query("SELECT l FROM Libro l WHERE YEAR(l.fechaPublicacion) = :anio")
    List<Libro> findByAnioPublicacion(@Param("anio") int anio);

    boolean existsByIsbn(String isbn);

    List<Libro> findByEditorialIdEditorial(Integer idEditorial);

    @Query("SELECT l.editorial.idEditorial, COUNT(l.idLibro) FROM Libro l WHERE l.editorial IS NOT NULL GROUP BY l.editorial.idEditorial")
    List<Object[]> contarLibrosPorEditorial();

    List<Libro> findByIsbn(String isbn);

    @Query("SELECT l FROM Libro l JOIN FETCH l.editorial")
    List<Libro> findAllConEditorial();

    @Query(value = "SELECT l FROM Libro l LEFT JOIN FETCH l.editorial",
            countQuery = "SELECT COUNT(l) FROM Libro l")
    Page<Libro> findAllConEditorial(Pageable pageable);

    @Query("SELECT DISTINCT l.estado FROM Libro l WHERE l.estado IS NOT NULL AND TRIM(l.estado) <> '' ORDER BY l.estado")
    List<String> findEstadosDistintos();
    /*
     * Búsqueda full-text delegada a MySQL (Requiere índice FULLTEXT en la BD)
     * 
     * @Query(value =
     * "SELECT * FROM libro WHERE MATCH(titulo, descripcion, contenido_texto) AGAINST(:keyword IN BOOLEAN MODE)"
     * , nativeQuery = true)
     * List<Libro> searchFullText(@Param("keyword") String keyword);
     */
}
