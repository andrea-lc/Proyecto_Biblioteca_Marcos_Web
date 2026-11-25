package com.bibliot.gestion_biblioteca.dto;

import com.bibliot.gestion_biblioteca.entities.Categoria;
import com.bibliot.gestion_biblioteca.entities.Libro;
import com.bibliot.gestion_biblioteca.entities.LibroCategoria;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public final class LibroCategoriaDtos {
    private LibroCategoriaDtos() {}

    public record Request(@NotNull @Positive Integer idLibro, @NotNull @Positive Integer idCategoria) {
        public LibroCategoria toEntity() {
            LibroCategoria x = new LibroCategoria(); Libro libro = new Libro(); libro.setIdLibro(idLibro);
            Categoria categoria = new Categoria(); categoria.setIdCategoria(idCategoria);
            x.setLibro(libro); x.setCategoria(categoria); return x;
        }
    }

    public record Response(Integer idLibro, Integer idCategoria) {
        public static Response desde(LibroCategoria x) { return new Response(x.getLibro().getIdLibro(), x.getCategoria().getIdCategoria()); }
    }
}
