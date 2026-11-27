package com.bibliot.gestion_biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public final class CirculacionPanelDtos {
    private CirculacionPanelDtos() {}

    public record EstadoRequest(
            @NotBlank(message = "El estado es obligatorio")
            @Size(max = 30, message = "El estado no debe superar 30 caracteres")
            String estado) {}

    public record PrestamoPanelResponse(
            Integer idPrestamo,
            Integer idUsuario,
            String nombreUsuario,
            String dniUsuario,
            String emailUsuario,
            Integer idEjemplar,
            String codigoInventario,
            String ubicacionEjemplar,
            Integer idLibro,
            String tituloLibro,
            String isbnLibro,
            List<String> categorias,
            LocalDateTime fechaPrestamo,
            LocalDateTime fechaVencimiento,
            LocalDateTime fechaDevolucion,
            String estadoAlmacenado,
            String estadoEfectivo,
            List<String> transicionesPermitidas) {}

    public record ReservaPanelResponse(
            Integer idReserva,
            Integer idUsuario,
            String nombreUsuario,
            String dniUsuario,
            String emailUsuario,
            String telefonoUsuario,
            Integer idLibro,
            String tituloLibro,
            String isbnLibro,
            List<String> categorias,
            LocalDateTime fechaReserva,
            LocalDateTime fechaExpiracion,
            String estadoAlmacenado,
            String estadoEfectivo,
            List<String> transicionesPermitidas) {}

    public record PaginaResponse<T>(
            List<T> items,
            int page,
            int size,
            long totalElements,
            int totalPages) {}
}
