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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PastOrPresent;

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

    @Column(name = "nombres", nullable = false, length = 100)
    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100, message = "Los nombres no deben superar 100 caracteres")
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no deben superar 100 caracteres")
    private String apellidos;

    @Column(name = "dni", nullable = false, unique = true, length = 20)

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "^[0-9]{8}$", message = "El DNI debe tener 8 dígitos")
    private String dni;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico no tiene un formato válido")
    @Size(max = 150)
    private String email;

    @Column(name = "telefono", length = 20)
    @Size(max = 20)
    @Pattern(regexp = "^[0-9]{9}$")
    private String telefono;

    @Column(name = "direccion", length = 200)
    @Size(max = 200)
    private String direccion;

    @Column(name = "password", nullable = false, length = 255)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 255, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @NotNull(message = "Debe asignarse un rol al usuario")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @Column(name = "fecha_registro")
    @PastOrPresent
    private LocalDateTime fechaRegistro;

    @Column(name = "activo")
    private Boolean activo;
}
