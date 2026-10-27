package com.bibliot.gestion_biblioteca.dto;

import com.bibliot.gestion_biblioteca.entities.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class CategoriaDtos {
    private CategoriaDtos() {}

    public record Request(Integer idCategoria, @NotBlank @Size(max = 100) String nombre,
            @Size(max = 300) String descripcion) {
        public Categoria toEntity() { Categoria x = new Categoria(); x.setIdCategoria(idCategoria); x.setNombre(nombre); x.setDescripcion(descripcion); return x; }
    }

    public record Response(Integer idCategoria, String nombre, String descripcion) {
        public static Response desde(Categoria x) { return new Response(x.getIdCategoria(), x.getNombre(), x.getDescripcion()); }
    }
}
