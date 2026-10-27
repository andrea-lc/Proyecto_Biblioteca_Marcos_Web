package com.bibliot.gestion_biblioteca.dto;

import com.bibliot.gestion_biblioteca.entities.Devolucion;
import com.bibliot.gestion_biblioteca.entities.Prestamo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public final class DevolucionDtos {
    private DevolucionDtos() {}

    public record Request(Integer idDevolucion, @NotNull @Positive Integer idPrestamo,
            @NotNull @PastOrPresent LocalDateTime fechaDevolucion, @Size(max = 2000) String observacion) {
        public Devolucion toEntity() {
            Devolucion x = new Devolucion(); x.setIdDevolucion(idDevolucion);
            Prestamo prestamo = new Prestamo(); prestamo.setIdPrestamo(idPrestamo);
            x.setPrestamo(prestamo); x.setFechaDevolucion(fechaDevolucion); x.setObservacion(observacion); return x;
        }
    }

    public record Response(Integer idDevolucion, Integer idPrestamo,
            LocalDateTime fechaDevolucion, String observacion) {
        public static Response desde(Devolucion x) {
            return new Response(x.getIdDevolucion(), x.getPrestamo().getIdPrestamo(),
                    x.getFechaDevolucion(), x.getObservacion());
        }
    }
}
