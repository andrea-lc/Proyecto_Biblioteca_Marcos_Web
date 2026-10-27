package com.bibliot.gestion_biblioteca.dto;

import com.bibliot.gestion_biblioteca.entities.Libro;
import java.time.LocalDate;
import java.util.List;

public record LibroResponse(
                Integer idLibro,
                String titulo,
                String isbn,
                Integer paginas,
                LocalDate fechaPublicacion,
                String estado,
                String descripcion,
                String contenidoTexto,
                Integer idEditorial,
                EditorialResponse editorial,
                List<String> autores,
                List<Integer> autorIds,
                List<String> categorias,
                List<Integer> categoriaIds,
                String formato,
                int cantidadEjemplares) {

        public record EditorialResponse(Integer idEditorial, String nombre) {
        }

        public static LibroResponse desde(Libro libro) {
                EditorialResponse editorial = libro.getEditorial() == null ? null
                                : new EditorialResponse(libro.getEditorial().getIdEditorial(),
                                                libro.getEditorial().getNombre());
                return new LibroResponse(libro.getIdLibro(), libro.getTitulo(), libro.getIsbn(), libro.getPaginas(),
                                libro.getFechaPublicacion(), libro.getEstado(), libro.getDescripcion(),
                                libro.getContenidoTexto(),
                                editorial == null ? null : editorial.idEditorial(), editorial,
                                List.of(), List.of(), List.of(), List.of(), "", 0);
        }
}
