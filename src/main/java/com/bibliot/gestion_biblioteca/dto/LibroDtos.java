package com.bibliot.gestion_biblioteca.dto;

import com.bibliot.gestion_biblioteca.entities.Libro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.ISBN;
import java.time.LocalDate;
import java.util.List;

public final class LibroDtos {
    private LibroDtos() {}

    public record Request(@NotBlank @Size(max = 255) String titulo, @ISBN String isbn,
            @Positive Integer paginas, @PastOrPresent LocalDate fechaPublicacion,
            @Size(max = 30) String estado, @Size(max = 5000) String descripcion,
            String contenidoTexto, @Positive Integer idEditorial,
            List<@Positive Integer> autorIds, List<@Positive Integer> categoriaIds) {}

    public record EditorialResponse(Integer idEditorial, String nombre) {}

    public record Response(Integer idLibro, String titulo, String isbn, Integer paginas,
            LocalDate fechaPublicacion, String estado, String descripcion, String contenidoTexto,
            Integer idEditorial, EditorialResponse editorial, List<String> autores,
            List<Integer> autorIds, List<String> categorias, List<Integer> categoriaIds,
            String formato, int cantidadEjemplares) {
        public static Response desde(Libro libro) {
            EditorialResponse editorial = libro.getEditorial() == null ? null
                    : new EditorialResponse(libro.getEditorial().getIdEditorial(), libro.getEditorial().getNombre());
            return new Response(libro.getIdLibro(), libro.getTitulo(), libro.getIsbn(), libro.getPaginas(),
                    libro.getFechaPublicacion(), libro.getEstado(), libro.getDescripcion(), libro.getContenidoTexto(),
                    editorial == null ? null : editorial.idEditorial(), editorial,
                    List.of(), List.of(), List.of(), List.of(), "", 0);
        }
    }
}
