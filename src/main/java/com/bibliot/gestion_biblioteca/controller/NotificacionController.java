package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.segurity.JwtAuthenticationDetails;
import com.bibliot.gestion_biblioteca.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {
    private final NotificacionService service;

    private Integer usuarioId(Authentication auth) {
        if (auth == null || !(auth.getDetails() instanceof JwtAuthenticationDetails details)
                || details.usuarioId() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "El token no contiene un usuario valido");
        }
        return details.usuarioId();
    }

    @GetMapping
    public List<NotificacionService.Vista> listar(Authentication auth) {
        return service.listar(usuarioId(auth));
    }

    @GetMapping("/no-leidas")
    public long noLeidas(Authentication auth) { return service.noLeidas(usuarioId(auth)); }

    @PostMapping("/{id}/leida")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void marcarLeida(@PathVariable Integer id, Authentication auth) {
        if (!service.marcarLeida(id, usuarioId(auth))) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(Authentication auth) { return service.conectar(usuarioId(auth)); }
}
