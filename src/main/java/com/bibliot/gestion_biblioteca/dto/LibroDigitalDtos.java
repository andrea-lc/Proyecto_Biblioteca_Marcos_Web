package com.bibliot.gestion_biblioteca.dto;

import com.bibliot.gestion_biblioteca.entities.Libro;
import com.bibliot.gestion_biblioteca.entities.LibroDigital;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import java.time.LocalDateTime;

public final class LibroDigitalDtos {
    private LibroDigitalDtos() {}

    public record Request(Integer idLibroDigital, @NotNull @Positive Integer idLibro,
            @Size(max = 255) @URL String archivoUrl, @Size(max = 30) String formato,
            @Positive Long tamanoArchivo, @NotNull Boolean disponible,
            @PastOrPresent LocalDateTime fechaRegistro) {
        public LibroDigital toEntity() {
            LibroDigital x = new LibroDigital(); x.setIdLibroDigital(idLibroDigital);
            Libro libro = new Libro(); libro.setIdLibro(idLibro); x.setLibro(libro);
            x.setArchivoUrl(archivoUrl); x.setFormato(formato); x.setTamanoArchivo(tamanoArchivo);
            x.setDisponible(disponible); x.setFechaRegistro(fechaRegistro); return x;
        }
    }

    public record Response(Integer idLibroDigital, Integer idLibro, String archivoUrl,
            String formato, Long tamanoArchivo, Boolean disponible, LocalDateTime fechaRegistro) {
        public static Response desde(LibroDigital x) {
            return new Response(x.getIdLibroDigital(), x.getLibro().getIdLibro(), x.getArchivoUrl(),
                    x.getFormato(), x.getTamanoArchivo(), x.getDisponible(), x.getFechaRegistro());
        }
    }
}
