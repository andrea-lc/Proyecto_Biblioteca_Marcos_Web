package com.bibliot.gestion_biblioteca.service;

import com.bibliot.gestion_biblioteca.dto.CirculacionPanelDtos.PaginaResponse;
import com.bibliot.gestion_biblioteca.dto.CirculacionPanelDtos.PrestamoPanelResponse;
import com.bibliot.gestion_biblioteca.dto.CirculacionPanelDtos.ReservaPanelResponse;
import com.bibliot.gestion_biblioteca.entities.Devolucion;
import com.bibliot.gestion_biblioteca.entities.Ejemplar;
import com.bibliot.gestion_biblioteca.entities.Libro;
import com.bibliot.gestion_biblioteca.entities.Prestamo;
import com.bibliot.gestion_biblioteca.entities.Reserva;
import com.bibliot.gestion_biblioteca.entities.Usuario;
import com.bibliot.gestion_biblioteca.repository.DevolucionRepository;
import com.bibliot.gestion_biblioteca.repository.EjemplarRepository;
import com.bibliot.gestion_biblioteca.repository.LibroCategoriaRepository;
import com.bibliot.gestion_biblioteca.repository.PrestamoRepository;
import com.bibliot.gestion_biblioteca.repository.ReservaRepository;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CirculacionPanelService {

    private final PrestamoRepository prestamoRepository;
    private final ReservaRepository reservaRepository;
    private final DevolucionRepository devolucionRepository;
    private final EjemplarRepository ejemplarRepository;
    private final LibroCategoriaRepository libroCategoriaRepository;

    @Value("${bibliot.prestamo.dias-plazo:14}")
    private int diasPlazoPrestamo;

    private static final int MAX_PAGE_SIZE = 100;

    @Transactional(readOnly = true)
    public PaginaResponse<PrestamoPanelResponse> listarPrestamos(int page, int size) {
        int safePage = Math.max(0, page);
        int safeSize = Math.min(MAX_PAGE_SIZE, Math.max(1, size));
        Page<Prestamo> resultado = prestamoRepository.findPanelPrestamos(PageRequest.of(safePage, safeSize));
        List<Prestamo> prestamos = resultado.getContent();

        List<Integer> libroIds = prestamos.stream()
                .map(p -> p.getEjemplar() != null && p.getEjemplar().getLibro() != null ? p.getEjemplar().getLibro().getIdLibro() : null)
                .filter(id -> id != null)
                .distinct()
                .toList();
        List<Integer> prestamoIds = prestamos.stream().map(Prestamo::getIdPrestamo).toList();

        Map<Integer, List<String>> categoriasPorLibro = cargarCategoriasPorLibro(libroIds);
        Map<Integer, LocalDateTime> devolucionesPorPrestamo = cargarDevolucionesPorPrestamo(prestamoIds);
        LocalDateTime ahora = LocalDateTime.now();

        List<PrestamoPanelResponse> items = prestamos.stream()
                .map(p -> mapearPrestamo(p, categoriasPorLibro, devolucionesPorPrestamo.get(p.getIdPrestamo()), ahora))
                .toList();

        return new PaginaResponse<>(items, resultado.getNumber(), resultado.getSize(),
                resultado.getTotalElements(), resultado.getTotalPages());
    }

    @Transactional(readOnly = true)
    public PaginaResponse<ReservaPanelResponse> listarReservas(int page, int size) {
        int safePage = Math.max(0, page);
        int safeSize = Math.min(MAX_PAGE_SIZE, Math.max(1, size));
        Page<Reserva> resultado = reservaRepository.findPanelReservas(PageRequest.of(safePage, safeSize));
        List<Reserva> reservas = resultado.getContent();

        List<Integer> libroIds = reservas.stream()
                .map(r -> r.getLibro() != null ? r.getLibro().getIdLibro() : null)
                .filter(id -> id != null)
                .distinct()
                .toList();

        Map<Integer, List<String>> categoriasPorLibro = cargarCategoriasPorLibro(libroIds);
        LocalDateTime ahora = LocalDateTime.now();

        List<ReservaPanelResponse> items = reservas.stream()
                .map(r -> mapearReserva(r, categoriasPorLibro, ahora))
                .toList();

        return new PaginaResponse<>(items, resultado.getNumber(), resultado.getSize(),
                resultado.getTotalElements(), resultado.getTotalPages());
    }

    @Transactional
    public PrestamoPanelResponse cambiarEstadoPrestamo(Integer idPrestamo, String estadoSolicitado) {
        Prestamo prestamo = prestamoRepository.findPanelById(idPrestamo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Préstamo no encontrado con ID: " + idPrestamo));

        String destino = normalizarEstadoPrestamoDestino(estadoSolicitado);
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime fechaDev = devolucionRepository.findByPrestamoIdPrestamo(idPrestamo)
                .map(Devolucion::getFechaDevolucion)
                .orElse(null);

        String estadoActual = calcularEstadoEfectivoPrestamo(prestamo, fechaDev, ahora);
        List<String> permitidas = transicionesPrestamo(estadoActual);

        if (!permitidas.contains(destino)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Transición no permitida: un préstamo en estado '" + estadoActual + "' no puede pasar a '" + destino + "'.");
        }

        if ("Activo".equals(destino) && "Por entregar".equals(estadoActual)) {
            LocalDateTime entrega = LocalDateTime.now();
            LocalDateTime vencimiento = entrega.plusDays(Math.max(1, diasPlazoPrestamo));
            prestamoRepository.actualizarEntrega(idPrestamo, "Activo", entrega, vencimiento);
            prestamo.setEstado("Activo");
            prestamo.setFechaPrestamo(entrega);
            prestamo.setFechaVencimiento(vencimiento);
            if (prestamo.getEjemplar() != null) {
                prestamo.getEjemplar().setEstado("Prestado");
                ejemplarRepository.save(prestamo.getEjemplar());
            }
            if (prestamo.getReserva() != null) {
                reservaRepository.actualizarEstado(prestamo.getReserva().getIdReserva(), "Atendida");
            }
        } else if ("Devuelto".equals(destino)) {
            if (fechaDev == null) {
                Devolucion devolucion = new Devolucion();
                devolucion.setPrestamo(prestamo);
                devolucion.setFechaDevolucion(LocalDateTime.now().minusSeconds(1));
                devolucion.setObservacion("Devolución registrada desde el panel de circulación.");
                devolucionRepository.save(devolucion);
                fechaDev = devolucion.getFechaDevolucion();
            }
            liberarEjemplar(prestamo.getEjemplar());
            prestamoRepository.actualizarEstado(idPrestamo, destino);
            prestamo.setEstado(destino);
        } else if ("Cancelado".equals(destino)) {
            liberarEjemplar(prestamo.getEjemplar());
            if ("Por entregar".equals(estadoActual) && prestamo.getReserva() != null) {
                reservaRepository.actualizarEstado(prestamo.getReserva().getIdReserva(), "Cancelada");
            }
            prestamoRepository.actualizarEstado(idPrestamo, destino);
            prestamo.setEstado(destino);
        }

        Integer idLibro = prestamo.getEjemplar() != null && prestamo.getEjemplar().getLibro() != null
                ? prestamo.getEjemplar().getLibro().getIdLibro() : null;
        Map<Integer, List<String>> cats = idLibro == null ? Map.of() : cargarCategoriasPorLibro(List.of(idLibro));
        return mapearPrestamo(prestamo, cats, fechaDev, LocalDateTime.now());
    }

    @Transactional
    public ReservaPanelResponse cambiarEstadoReserva(Integer idReserva, String estadoSolicitado) {
        Reserva reserva = reservaRepository.findPanelById(idReserva)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada con ID: " + idReserva));

        String destino = normalizarEstadoReservaDestino(estadoSolicitado);
        LocalDateTime ahora = LocalDateTime.now();
        String estadoActual = calcularEstadoEfectivoReserva(reserva, ahora);
        List<String> permitidas = transicionesReserva(estadoActual);

        if (!permitidas.contains(destino)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Transición no permitida: una reserva en estado '" + estadoActual + "' no puede pasar a '" + destino + "'.");
        }

        if ("Confirmada".equals(destino)) {
            if (prestamoRepository.findByReservaIdReserva(idReserva).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Esta reserva ya tiene un préstamo asociado.");
            }
            if (reserva.getLibro() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La reserva no tiene un libro asociado.");
            }
            List<Ejemplar> disponibles = ejemplarRepository.findDisponiblesParaApartar(
                    reserva.getLibro().getIdLibro(), PageRequest.of(0, 1));
            if (disponibles.isEmpty()) {
                boolean tieneEjemplares = !ejemplarRepository.findByLibroIdLibro(reserva.getLibro().getIdLibro()).isEmpty();
                String motivo = tieneEjemplares
                        ? "No hay ejemplares disponibles de \"" + reserva.getLibro().getTitulo() + "\" (todos están prestados o reservados)."
                        : "El libro \"" + reserva.getLibro().getTitulo() + "\" no tiene ejemplares físicos registrados. Registra al menos un ejemplar disponible para confirmar la reserva.";
                throw new ResponseStatusException(HttpStatus.CONFLICT, motivo);
            }
            Ejemplar ejemplar = disponibles.get(0);
            ejemplar.setEstado("Reservado");
            ejemplarRepository.save(ejemplar);

            Prestamo nuevoPrestamo = new Prestamo();
            nuevoPrestamo.setUsuario(reserva.getUsuario());
            nuevoPrestamo.setEjemplar(ejemplar);
            nuevoPrestamo.setReserva(reserva);
            nuevoPrestamo.setFechaPrestamo(null);
            nuevoPrestamo.setFechaVencimiento(null);
            nuevoPrestamo.setEstado("Por entregar");
            prestamoRepository.save(nuevoPrestamo);
        } else if ("Cancelada".equals(destino)) {
            liberarPrestamoPendienteDeReserva(idReserva);
        }

        reservaRepository.actualizarEstado(idReserva, destino);
        reserva.setEstado(destino);

        Integer idLibro = reserva.getLibro() != null ? reserva.getLibro().getIdLibro() : null;
        Map<Integer, List<String>> cats = idLibro == null ? Map.of() : cargarCategoriasPorLibro(List.of(idLibro));
        return mapearReserva(reserva, cats, LocalDateTime.now());
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void liberarReservasConfirmadasExpiradas() {
        List<Reserva> expiradas = reservaRepository.findConfirmadasExpiradas(LocalDateTime.now());
        for (Reserva r : expiradas) {
            liberarPrestamoPendienteDeReserva(r.getIdReserva());
            reservaRepository.actualizarEstado(r.getIdReserva(), "Expirada");
        }
    }

    private void liberarPrestamoPendienteDeReserva(Integer idReserva) {
        prestamoRepository.findByReservaIdReserva(idReserva).ifPresent(prestamo -> {
            if (clave(prestamo.getEstado()).contains("por entregar")) {
                liberarEjemplar(prestamo.getEjemplar());
                prestamoRepository.actualizarEstado(prestamo.getIdPrestamo(), "Cancelado");
            }
        });
    }

    private void liberarEjemplar(Ejemplar ejemplar) {
        if (ejemplar != null) {
            ejemplar.setEstado("Disponible");
            ejemplarRepository.save(ejemplar);
        }
    }

    private String normalizarEstadoPrestamoDestino(String raw) {
        String key = clave(raw);
        return switch (key) {
            case "activo", "confirmar entrega", "entregado" -> "Activo";
            case "devuelto" -> "Devuelto";
            case "cancelado" -> "Cancelado";
            case "vencido" -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El estado 'Vencido' se calcula automáticamente por fecha y no puede asignarse manualmente.");
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Estado de préstamo no válido para cambio manual: '" + raw + "'. Opciones válidas: Activo (Confirmar entrega), Devuelto, Cancelado.");
        };
    }

    private String normalizarEstadoReservaDestino(String raw) {
        String key = clave(raw);
        return switch (key) {
            case "confirmada" -> "Confirmada";
            case "cancelada" -> "Cancelada";
            case "atendida" -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La reserva pasa a 'Atendida' automáticamente al confirmar la entrega del préstamo en la pestaña Préstamos.");
            case "expirada" -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El estado 'Expirada' se calcula automáticamente por fecha y no puede asignarse manualmente.");
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Estado de reserva no válido para cambio manual: '" + raw + "'. Opciones válidas: Confirmada, Cancelada.");
        };
    }

    private String calcularEstadoEfectivoPrestamo(Prestamo p, LocalDateTime fechaDev, LocalDateTime ahora) {
        String k = clave(p.getEstado());
        if (fechaDev != null || k.contains("devuelt")) return "Devuelto";
        if (k.contains("cancelad")) return "Cancelado";
        if (k.contains("por entregar")) return "Por entregar";
        if (k.contains("vencid") || (p.getFechaVencimiento() != null && p.getFechaVencimiento().isBefore(ahora))) {
            return "Vencido";
        }
        return "Activo";
    }

    private String calcularEstadoEfectivoReserva(Reserva r, LocalDateTime ahora) {
        String k = clave(r.getEstado());
        if (k.contains("atendid") || k.contains("complet")) return "Atendida";
        if (k.contains("cancelad")) return "Cancelada";
        if (k.contains("expirad") || (r.getFechaExpiracion() != null && r.getFechaExpiracion().isBefore(ahora))) {
            return "Expirada";
        }
        if (k.contains("confirmad")) return "Confirmada";
        return "Pendiente";
    }

    private List<String> transicionesPrestamo(String estadoEfectivo) {
        return switch (estadoEfectivo) {
            case "Por entregar" -> List.of("Activo", "Cancelado");
            case "Activo", "Vencido" -> List.of("Devuelto", "Cancelado");
            default -> List.of();
        };
    }

    private List<String> transicionesReserva(String estadoEfectivo) {
        return switch (estadoEfectivo) {
            case "Pendiente" -> List.of("Confirmada", "Cancelada");
            case "Confirmada", "Expirada" -> List.of("Cancelada");
            default -> List.of();
        };
    }

    private PrestamoPanelResponse mapearPrestamo(Prestamo p, Map<Integer, List<String>> categoriasPorLibro,
            LocalDateTime fechaDev, LocalDateTime ahora) {
        Usuario u = p.getUsuario();
        Ejemplar e = p.getEjemplar();
        Libro l = e != null ? e.getLibro() : null;
        String efectivo = calcularEstadoEfectivoPrestamo(p, fechaDev, ahora);
        return new PrestamoPanelResponse(
                p.getIdPrestamo(),
                u != null ? u.getIdUsuario() : null,
                u != null ? (u.getNombres() + " " + u.getApellidos()).trim() : "Usuario sin registrar",
                u != null ? u.getDni() : null,
                u != null ? u.getEmail() : null,
                e != null ? e.getIdEjemplar() : null,
                e != null ? e.getCodigoInventario() : null,
                e != null ? e.getUbicacion() : null,
                l != null ? l.getIdLibro() : null,
                l != null ? l.getTitulo() : "Libro sin título",
                l != null ? l.getIsbn() : null,
                l != null ? categoriasPorLibro.getOrDefault(l.getIdLibro(), List.of()) : List.of(),
                p.getFechaPrestamo(),
                p.getFechaVencimiento(),
                fechaDev,
                p.getEstado(),
                efectivo,
                transicionesPrestamo(efectivo)
        );
    }

    private ReservaPanelResponse mapearReserva(Reserva r, Map<Integer, List<String>> categoriasPorLibro,
            LocalDateTime ahora) {
        Usuario u = r.getUsuario();
        Libro l = r.getLibro();
        String efectivo = calcularEstadoEfectivoReserva(r, ahora);
        return new ReservaPanelResponse(
                r.getIdReserva(),
                u != null ? u.getIdUsuario() : null,
                u != null ? (u.getNombres() + " " + u.getApellidos()).trim() : "Usuario sin registrar",
                u != null ? u.getDni() : null,
                u != null ? u.getEmail() : null,
                u != null ? u.getTelefono() : null,
                l != null ? l.getIdLibro() : null,
                l != null ? l.getTitulo() : "Libro sin título",
                l != null ? l.getIsbn() : null,
                l != null ? categoriasPorLibro.getOrDefault(l.getIdLibro(), List.of()) : List.of(),
                r.getFechaReserva(),
                r.getFechaExpiracion(),
                r.getEstado(),
                efectivo,
                transicionesReserva(efectivo)
        );
    }

    private Map<Integer, List<String>> cargarCategoriasPorLibro(List<Integer> libroIds) {
        if (libroIds == null || libroIds.isEmpty()) return Collections.emptyMap();
        Map<Integer, List<String>> mapa = new HashMap<>();
        for (Object[] fila : libroCategoriaRepository.categoriasDeLibros(libroIds)) {
            Integer idLibro = ((Number) fila[0]).intValue();
            String nombre = String.valueOf(fila[1]);
            mapa.computeIfAbsent(idLibro, ignored -> new ArrayList<>()).add(nombre);
        }
        return mapa;
    }

    private Map<Integer, LocalDateTime> cargarDevolucionesPorPrestamo(List<Integer> prestamoIds) {
        if (prestamoIds == null || prestamoIds.isEmpty()) return Collections.emptyMap();
        Map<Integer, LocalDateTime> mapa = new HashMap<>();
        for (Object[] fila : devolucionRepository.fechasDevolucionDePrestamos(prestamoIds)) {
            Integer idPrestamo = ((Number) fila[0]).intValue();
            LocalDateTime fecha = (LocalDateTime) fila[1];
            mapa.put(idPrestamo, fecha);
        }
        return mapa;
    }

    private static String clave(String texto) {
        if (texto == null) return "";
        return Normalizer.normalize(texto.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
    }
}
