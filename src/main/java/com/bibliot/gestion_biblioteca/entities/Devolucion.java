package com.bibliot.gestion_biblioteca.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "devolucion")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_devolucion")
    private Integer idDevolucion;

    @NotNull(message = "Debe asociarse la devolución a un préstamo")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prestamo", nullable = false, unique = true)
    private Prestamo prestamo;

    @Column(name = "fecha_devolucion", nullable = false)
    @NotNull(message = "La fecha de devolución es obligatoria")
    private LocalDateTime fechaDevolucion;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;
}
