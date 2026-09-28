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

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "devolucion")
@Getter
@Setter
@NoArgsConstructor
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_devolucion")
    private Integer idDevolucion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prestamo", nullable = false, unique = true)
    private Prestamo prestamo;

    @Column(name = "fecha_devolucion", nullable = false)
    private LocalDateTime fechaDevolucion;

    @Column(name = "observacion", length = 255)
    private String observacion;

    @Column(name = "tiene_sancion")
    private Boolean tieneSancion;

    @Column(name = "fecha_fin_sancion")
    private LocalDate fechaFinSancion;
}
