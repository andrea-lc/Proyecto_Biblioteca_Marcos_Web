package com.bibliot.gestion_biblioteca.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroDtos {
    private String nombres;
    private String dni;
    private String correo;
    private String telefono;
    private String direccion;
    private String password;
    private String confirmarPassword;
}