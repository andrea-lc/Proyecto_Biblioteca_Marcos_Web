package com.bibliot.gestion_biblioteca.dto;

import com.bibliot.gestion_biblioteca.entities.Devolucion;
import com.bibliot.gestion_biblioteca.entities.Sancion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public final class SancionDtos {
    private SancionDtos() {}

    public record Request(Integer idSancion, @NotNull @Positive Integer idDevolucion,
            @Size(max = 255) String motivo, @NotNull @PastOrPresent LocalDate fechaInicio,
            @NotNull LocalDate fechaFin, @NotBlank @Size(max = 30) String estado) {
        public Sancion toEntity() {
            Sancion x = new Sancion(); x.setIdSancion(idSancion);
            Devolucion devolucion = new Devolucion(); devolucion.setIdDevolucion(idDevolucion);
            x.setDevolucion(devolucion); x.setMotivo(motivo); x.setFechaInicio(fechaInicio);
            x.setFechaFin(fechaFin); x.setEstado(estado); return x;
        }
    }

    public record Response(Integer idSancion, Integer idDevolucion, String motivo,
            LocalDate fechaInicio, LocalDate fechaFin, String estado) {
        public static Response desde(Sancion x) {
            return new Response(x.getIdSancion(), x.getDevolucion().getIdDevolucion(), x.getMotivo(),
                    x.getFechaInicio(), x.getFechaFin(), x.getEstado());
        }
    }
}
