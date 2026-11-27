package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.UsuarioDtos;
import com.bibliot.gestion_biblioteca.entities.Prestamo;
import com.bibliot.gestion_biblioteca.entities.Reserva;
import com.bibliot.gestion_biblioteca.entities.Usuario;
import com.bibliot.gestion_biblioteca.repository.PrestamoRepository;
import com.bibliot.gestion_biblioteca.repository.ReservaRepository;
import com.bibliot.gestion_biblioteca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@RestController
@Transactional(readOnly = true)
@RequestMapping("/api/lector/me")
@RequiredArgsConstructor
public class MiCuentaController {

    private final UsuarioRepository usuarios;
    private final PrestamoRepository prestamos;
    private final ReservaRepository reservas;

    public record PrestamoVista(Integer idPrestamo, String tituloLibro,
                                LocalDateTime fechaPrestamo, LocalDateTime fechaVencimiento, String estado) {}

    public record ReservaVista(Integer idReserva, String tituloLibro,
                               LocalDateTime fechaReserva, LocalDateTime fechaExpiracion, String estado) {}

    // auth.getName() = email que viene en el token
    private Usuario actual(Authentication auth) {
        return usuarios.findByEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    @GetMapping
    public UsuarioDtos.Response perfil(Authentication auth) {
        return UsuarioDtos.Response.desde(actual(auth));
    }

    @GetMapping("/prestamos")
    public List<PrestamoVista> misPrestamos(Authentication auth) {
        Usuario u = actual(auth);
        return prestamos.findByUsuarioIdUsuario(u.getIdUsuario()).stream()
                .sorted(Comparator.comparing(Prestamo::getFechaPrestamo,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(p -> new PrestamoVista(
                        p.getIdPrestamo(),
                        p.getEjemplar() != null && p.getEjemplar().getLibro() != null
                                ? p.getEjemplar().getLibro().getTitulo() : null,
                        p.getFechaPrestamo(), p.getFechaVencimiento(), p.getEstado()))
                .toList();
    }

    @GetMapping("/reservas")
    public List<ReservaVista> misReservas(Authentication auth) {
        Usuario u = actual(auth);
        return reservas.findByUsuarioIdUsuario(u.getIdUsuario()).stream()
                .sorted(Comparator.comparing(Reserva::getFechaReserva,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(r -> new ReservaVista(
                        r.getIdReserva(),
                        r.getLibro() != null ? r.getLibro().getTitulo() : null,
                        r.getFechaReserva(), r.getFechaExpiracion(), r.getEstado()))
                .toList();
    }
}