package com.bibliot.gestion_biblioteca.dto;

import com.bibliot.gestion_biblioteca.entities.Ejemplar;
import com.bibliot.gestion_biblioteca.entities.Prestamo;
import com.bibliot.gestion_biblioteca.entities.Usuario;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public final class PrestamoDtos {
    private PrestamoDtos() {}

    public record Request(Integer idPrestamo, @NotNull @Positive Integer idUsuario,
            @NotNull @Positive Integer idEjemplar, @NotNull @PastOrPresent LocalDateTime fechaPrestamo,
            @NotNull @FutureOrPresent LocalDateTime fechaVencimiento,
            @NotBlank @Size(max = 30) String estado) {
        public Prestamo toEntity() {
            Prestamo x = new Prestamo(); x.setIdPrestamo(idPrestamo);
            Usuario usuario = new Usuario(); usuario.setIdUsuario(idUsuario);
            Ejemplar ejemplar = new Ejemplar(); ejemplar.setIdEjemplar(idEjemplar);
            x.setUsuario(usuario); x.setEjemplar(ejemplar); x.setFechaPrestamo(fechaPrestamo);
            x.setFechaVencimiento(fechaVencimiento); x.setEstado(estado); return x;
        }
    }

    public record Response(Integer idPrestamo, Integer idUsuario, Integer idEjemplar,
            LocalDateTime fechaPrestamo, LocalDateTime fechaVencimiento, String estado,
            String tituloLibro) {
        public static Response desde(Prestamo x) {
            return new Response(x.getIdPrestamo(), x.getUsuario().getIdUsuario(),
                    x.getEjemplar().getIdEjemplar(), x.getFechaPrestamo(), x.getFechaVencimiento(),
                    x.getEstado(), x.getEjemplar().getLibro() == null ? null : x.getEjemplar().getLibro().getTitulo());
        }
    }
}
