package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.CirculacionPanelDtos.EstadoRequest;
import com.bibliot.gestion_biblioteca.dto.CirculacionPanelDtos.PaginaResponse;
import com.bibliot.gestion_biblioteca.dto.CirculacionPanelDtos.PrestamoPanelResponse;
import com.bibliot.gestion_biblioteca.dto.CirculacionPanelDtos.ReservaPanelResponse;
import com.bibliot.gestion_biblioteca.service.CirculacionPanelService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bibliotecario/circulacion")
public class CirculacionPanelController {

    private final CirculacionPanelService circulacionService;

    @GetMapping("/prestamos")
    public PaginaResponse<PrestamoPanelResponse> listarPrestamos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        return circulacionService.listarPrestamos(page, size);
    }

    @PatchMapping("/prestamos/{id}/estado")
    public PrestamoPanelResponse cambiarEstadoPrestamo(
            @PathVariable Integer id,
            @Valid @RequestBody EstadoRequest request) {
        return circulacionService.cambiarEstadoPrestamo(id, request.estado());
    }

    @GetMapping("/reservas")
    public PaginaResponse<ReservaPanelResponse> listarReservas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        return circulacionService.listarReservas(page, size);
    }

    @PatchMapping("/reservas/{id}/estado")
    public ReservaPanelResponse cambiarEstadoReserva(
            @PathVariable Integer id,
            @Valid @RequestBody EstadoRequest request) {
        return circulacionService.cambiarEstadoReserva(id, request.estado());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleStatusException(ResponseStatusException ex) {
        String msg = ex.getReason() != null && !ex.getReason().isBlank()
                ? ex.getReason()
                : "No se pudo completar la operación.";
        return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message", msg));
    }
}
