package com.bibliot.gestion_biblioteca.dto;

import com.bibliot.gestion_biblioteca.entities.Ejemplar;
import com.bibliot.gestion_biblioteca.entities.Libro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public final class EjemplarDtos {
    private EjemplarDtos() {}

    public record Request(Integer idEjemplar, @NotNull @Positive Integer idLibro,
            @NotBlank @Size(max = 100) String codigoInventario, @Size(max = 100) String ubicacion,
            @NotBlank @Size(max = 30) String estado, @PastOrPresent LocalDate fechaAdquisicion) {
        public Ejemplar toEntity() {
            Ejemplar x = new Ejemplar(); x.setIdEjemplar(idEjemplar);
            Libro libro = new Libro(); libro.setIdLibro(idLibro); x.setLibro(libro);
            x.setCodigoInventario(codigoInventario); x.setUbicacion(ubicacion); x.setEstado(estado);
            x.setFechaAdquisicion(fechaAdquisicion); return x;
        }
    }

    public record Response(Integer idEjemplar, Integer idLibro, String codigoInventario,
            String ubicacion, String estado, LocalDate fechaAdquisicion) {
        public static Response desde(Ejemplar x) {
            return new Response(x.getIdEjemplar(), x.getLibro().getIdLibro(), x.getCodigoInventario(),
                    x.getUbicacion(), x.getEstado(), x.getFechaAdquisicion());
        }
    }
}
