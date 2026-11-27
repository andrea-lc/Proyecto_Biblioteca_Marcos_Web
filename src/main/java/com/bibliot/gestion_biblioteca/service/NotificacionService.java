package com.bibliot.gestion_biblioteca.service;

import com.bibliot.gestion_biblioteca.entities.*;
import com.bibliot.gestion_biblioteca.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class NotificacionService {
    private final NotificacionRepository notificaciones;
    private final UsuarioRepository usuarios;
    private final PrestamoRepository prestamos;
    private final Map<Integer, CopyOnWriteArrayList<SseEmitter>> conexiones = new ConcurrentHashMap<>();

    public record Vista(Integer id, String tipo, String titulo, String mensaje, String urlDestino,
                        LocalDateTime fechaCreacion, boolean leida) {}

    @Transactional(readOnly = true)
    public List<Vista> listar(Integer usuarioId) {
        return notificaciones.findByUsuarioIdUsuarioOrderByFechaCreacionDesc(usuarioId).stream()
                .map(n -> new Vista(n.getIdNotificacion(), n.getTipo(), n.getTitulo(), n.getMensaje(),
                        n.getUrlDestino(), n.getFechaCreacion(), n.getFechaLectura() != null))
                .toList();
    }

    public long noLeidas(Integer usuarioId) {
        return notificaciones.countByUsuarioIdUsuarioAndFechaLecturaIsNull(usuarioId);
    }

    @Transactional
    public boolean marcarLeida(Integer id, Integer usuarioId) {
        return notificaciones.findById(id).filter(n -> n.getUsuario().getIdUsuario().equals(usuarioId))
                .map(n -> {
                    if (n.getFechaLectura() == null) n.setFechaLectura(LocalDateTime.now());
                    return true;
                }).orElse(false);
    }

    public SseEmitter conectar(Integer usuarioId) {
        SseEmitter emitter = new SseEmitter(0L);
        var lista = conexiones.computeIfAbsent(usuarioId, ignored -> new CopyOnWriteArrayList<>());
        lista.add(emitter);
        Runnable quitar = () -> {
            lista.remove(emitter);
            if (lista.isEmpty()) conexiones.remove(usuarioId, lista);
        };
        emitter.onCompletion(quitar);
        emitter.onTimeout(quitar);
        emitter.onError(error -> quitar.run());
        try { emitter.send(SseEmitter.event().name("connected").data("ready")); }
        catch (IOException ex) { quitar.run(); }
        return emitter;
    }

    @Transactional
    public void libroNuevo(Libro libro) {
        List<Usuario> lectores = usuarios.findByRolNombreIn(List.of("LECTOR"));
        for (Usuario lector : lectores) {
            crear(lector, "libro:" + libro.getIdLibro(), "LIBRO_NUEVO", "Nuevo libro disponible",
                    "Se agregó “" + libro.getTitulo() + "” al catálogo.", "/explorar");
        }
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void revisarVencimientos() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limite = ahora.plusHours(24);
        List<Prestamo> pendientes = prestamos.findActivosPendientesDeVencimiento(limite);
        if (pendientes.isEmpty()) return;

        List<Usuario> staff = usuarios.findByRolNombreIn(List.of("ADMINISTRADOR", "BIBLIOTECARIO"));
        List<AlertaVencimiento> alertas = new ArrayList<>();
        for (Prestamo prestamo : pendientes) {
            LocalDateTime vence = prestamo.getFechaVencimiento();
            if (vence == null) continue;
            boolean vencido = !vence.isAfter(ahora);
            String claveEvento = "pr\u00E9stamo:" + prestamo.getIdPrestamo()
                    + (vencido ? ":vencido" : ":vence-pronto");
            String tituloLibro = prestamo.getEjemplar().getLibro().getTitulo();
            String nombreLector = nombreCompleto(prestamo.getUsuario());

            if (vencido) {
                String mensajeLector = "El plazo para devolver \u201C" + tituloLibro
                        + "\u201D ya venci\u00F3. Tu cuenta ser\u00E1 desactivada hasta que lo devuelvas.";
                alertas.add(new AlertaVencimiento(prestamo.getUsuario(), claveEvento, "PRESTAMO_VENCIDO",
                        "Pr\u00E9stamo vencido", mensajeLector, "/mis-prestamos"));
                for (Usuario usuario : staff) {
                    alertas.add(new AlertaVencimiento(usuario, claveEvento, "PRESTAMO_VENCIDO",
                            "Pr\u00E9stamo vencido", nombreLector + " no devolvi\u00F3 \u201C" + tituloLibro
                                    + "\u201D antes del " + vence + ".", "/dashboard"));
                }
            } else {
                for (Usuario usuario : staff) {
                    alertas.add(new AlertaVencimiento(usuario, claveEvento, "PRESTAMO_POR_VENCER",
                            "Pr\u00E9stamo pr\u00F3ximo a vencer", nombreLector + " debe devolver \u201C"
                                    + tituloLibro + "\u201D antes del " + vence + ".", "/dashboard"));
                }
            }
        }

        if (alertas.isEmpty()) return;
        List<String> clavesEvento = alertas.stream().map(AlertaVencimiento::eventKey).distinct().toList();
        Set<DestinatarioEvento> existentes = new HashSet<>();
        for (Object[] fila : notificaciones.findExistingEvents(clavesEvento)) {
            existentes.add(new DestinatarioEvento((String) fila[0], (Integer) fila[1]));
        }

        List<Notificacion> nuevas = new ArrayList<>();
        Set<Integer> usuariosNotificados = new HashSet<>();
        for (AlertaVencimiento alerta : alertas) {
            Integer usuarioId = alerta.usuario().getIdUsuario();
            if (existentes.contains(new DestinatarioEvento(alerta.eventKey(), usuarioId))) continue;

            Notificacion notificacion = new Notificacion();
            notificacion.setUsuario(alerta.usuario());
            notificacion.setEventKey(alerta.eventKey());
            notificacion.setTipo(alerta.tipo());
            notificacion.setTitulo(alerta.titulo());
            notificacion.setMensaje(alerta.mensaje());
            notificacion.setUrlDestino(alerta.destino());
            nuevas.add(notificacion);
            usuariosNotificados.add(usuarioId);
        }
        if (nuevas.isEmpty()) return;

        notificaciones.saveAll(nuevas);
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            for (Integer usuarioId : usuariosNotificados) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override public void afterCommit() { enviar(usuarioId); }
                });
            }
        } else {
            usuariosNotificados.forEach(this::enviar);
        }
    }

    private String nombreCompleto(Usuario usuario) {
        return (usuario.getNombres() + " " + usuario.getApellidos()).trim();
    }

    private record AlertaVencimiento(Usuario usuario, String eventKey, String tipo, String titulo,
            String mensaje, String destino) {}

    private record DestinatarioEvento(String eventKey, Integer usuarioId) {}
    private void crear(Usuario usuario, String eventKey, String tipo, String titulo, String mensaje, String destino) {
        if (notificaciones.existsByEventKeyAndUsuarioIdUsuario(eventKey, usuario.getIdUsuario())) return;
        Notificacion n = new Notificacion();
        n.setUsuario(usuario);
        n.setEventKey(eventKey);
        n.setTipo(tipo);
        n.setTitulo(titulo);
        n.setMensaje(mensaje);
        n.setUrlDestino(destino);
        notificaciones.save(n);
        Integer usuarioId = usuario.getIdUsuario();
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override public void afterCommit() { enviar(usuarioId); }
            });
        } else enviar(usuarioId);
    }

    private void enviar(Integer usuarioId) {
        var lista = conexiones.get(usuarioId);
        if (lista == null) return;
        for (SseEmitter emitter : lista) {
            try { emitter.send(SseEmitter.event().name("notification").data("refresh")); }
            catch (IOException ex) { emitter.completeWithError(ex); lista.remove(emitter); }
        }
    }
}
