package com.bibliot.gestion_biblioteca.dto;

import com.bibliot.gestion_biblioteca.entities.Libro;
import com.bibliot.gestion_biblioteca.entities.Reserva;
import com.bibliot.gestion_biblioteca.entities.Usuario;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public final class ReservaDtos {
    private ReservaDtos() {}

    public record Request(Integer idReserva, @NotNull @Positive Integer idUsuario,
            @NotNull @Positive Integer idLibro, @NotNull @PastOrPresent LocalDateTime fechaReserva,
            @NotNull @FutureOrPresent LocalDateTime fechaExpiracion,
            @NotBlank @Size(max = 30) String estado) {
        public Reserva toEntity() {
            Reserva x = new Reserva(); x.setIdReserva(idReserva);
            Usuario usuario = new Usuario(); usuario.setIdUsuario(idUsuario);
            Libro libro = new Libro(); libro.setIdLibro(idLibro);
            x.setUsuario(usuario); x.setLibro(libro); x.setFechaReserva(fechaReserva);
            x.setFechaExpiracion(fechaExpiracion); x.setEstado(estado); return x;
        }
    }

    public record Response(Integer idReserva, Integer idUsuario, Integer idLibro,
            LocalDateTime fechaReserva, LocalDateTime fechaExpiracion, String estado) {
        public static Response desde(Reserva x) {
            return new Response(x.getIdReserva(), x.getUsuario().getIdUsuario(), x.getLibro().getIdLibro(),
                    x.getFechaReserva(), x.getFechaExpiracion(), x.getEstado());
        }
    }
}
