package com.bibliot.gestion_biblioteca.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion", uniqueConstraints = @UniqueConstraint(name = "uk_notificacion_evento_usuario", columnNames = {"event_key", "id_usuario"}))
@Getter @Setter @NoArgsConstructor
public class Notificacion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Integer idNotificacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "event_key", nullable = false, length = 180)
    private String eventKey;

    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo;

    @Column(name = "titulo", nullable = false, length = 160)
    private String titulo;

    @Column(name = "mensaje", nullable = false, length = 1000)
    private String mensaje;

    @Column(name = "url_destino", length = 300)
    private String urlDestino;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_lectura")
    private LocalDateTime fechaLectura;
}
