package com.bibliot.gestion_biblioteca.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "prestamo")
@Getter
@Setter
@NoArgsConstructor
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestamo")
    private Integer idPrestamo;

    @NotNull(message = "Debe indicarse el usuario del préstamo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @NotNull(message = "Debe indicarse el ejemplar prestado")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ejemplar", nullable = false)
    private Ejemplar ejemplar;

    @Column(name = "fecha_prestamo", nullable = false)
    @NotNull(message = "La fecha del préstamo es obligatoria")
    @jakarta.validation.constraints.PastOrPresent(message = "La fecha del préstamo no puede ser futura")
    private LocalDateTime fechaPrestamo;

    @Column(name = "fecha_vencimiento", nullable = false)
    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @FutureOrPresent(message = "El vencimiento debe ser actual o futuro")
    private LocalDateTime fechaVencimiento;

    @Column(name = "estado", nullable = false, length = 30)
    @NotBlank(message = "El estado del préstamo es obligatorio")
    @Size(max = 30)
    private String estado;
}
