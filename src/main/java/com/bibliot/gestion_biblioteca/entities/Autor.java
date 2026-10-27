package com.bibliot.gestion_biblioteca.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "autor")
@Getter
@Setter
@NoArgsConstructor
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_autor")
    private Integer idAutor;

    @Column(name = "nombres",nullable = false, length = 100)
    @NotBlank(message = "Los nombres del autor son obligatorios")
    @Size(max = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    @NotBlank(message = "Los apellidos del autor son obligatorios")
    @Size(max = 100)
    private String apellidos;

    @Column(name = "nacionalidad", length = 100)
    @Size(max = 100)
    private String nacionalidad;

    @Column(name = "biografia", columnDefinition = "TEXT")
    @Size(max = 5000, message = "La biografía no debe superar 5000 caracteres")
    private String biografia;
}
