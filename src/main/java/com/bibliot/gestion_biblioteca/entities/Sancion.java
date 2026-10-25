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
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "sancion")
@Getter
@Setter
@NoArgsConstructor
public class Sancion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sancion")
    private Integer idSancion;

    @NotNull(message = "Debe asociarse la sanción a una devolución")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_devolucion", nullable = false, unique = true)
    private Devolucion devolucion;

    @Column(name = "motivo", length = 255)
    @Size(max=255)
    private String motivo;

    @Column(name = "fecha_inicio")
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;

    @Column(name = "estado", length = 30)
    @NotBlank(message = "El estado de la sanción es obligatorio")
    @Size(max = 30)
    private String estado;
}
