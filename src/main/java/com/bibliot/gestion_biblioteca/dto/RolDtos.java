package com.bibliot.gestion_biblioteca.dto;

import com.bibliot.gestion_biblioteca.entities.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class RolDtos {
    private RolDtos() {}

    public record Request(Integer idRol, @NotBlank @Size(max = 50) String nombre) {
        public Rol toEntity() { Rol rol = new Rol(); rol.setIdRol(idRol); rol.setNombre(nombre); return rol; }
    }

    public record Response(Integer idRol, String nombre) {
        public static Response desde(Rol rol) { return new Response(rol.getIdRol(), rol.getNombre()); }
    }
}
