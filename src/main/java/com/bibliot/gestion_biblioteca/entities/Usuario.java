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
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @NotEmpty (message = "El campo nombres no puede estar vacío")
    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @NotEmpty (message = "El campo apellidos no puede estar vacío")
    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @NotEmpty (message = "El campo DNI no puede estar vacío")
    @Min (value = 8, message = "El campo DNI debe tener al menos 8 caracteres")
    @Column(name = "dni", nullable = false, unique = true, length = 20)
    private String dni;

    @NotBlank (message = "El campo email no puede estar vacío")
    @Email (message = "El campo email debe ser un correo electrónico válido")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank (message = "El campo teléfono no puede estar vacío")
    @Size (min = 9, max = 20, message = "El campo teléfono debe tener 9 numeros")
    @Column(name = "telefono", length = 20)
    private String telefono;


    @Column(name = "direccion", length = 200)
    private String direccion;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @NotBlank (message = "El campo estado no puede estar vacío")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @FutureOrPresent (message = "La fecha de registro no puede ser en el pasado")
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "activo")
    private Boolean activo;
}
