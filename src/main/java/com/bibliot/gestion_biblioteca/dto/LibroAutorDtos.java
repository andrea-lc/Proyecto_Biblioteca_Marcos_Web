package com.bibliot.gestion_biblioteca.dto;

import com.bibliot.gestion_biblioteca.entities.Autor;
import com.bibliot.gestion_biblioteca.entities.Libro;
import com.bibliot.gestion_biblioteca.entities.LibroAutor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public final class LibroAutorDtos {
    private LibroAutorDtos() {}

    public record Request(@NotNull @Positive Integer idLibro, @NotNull @Positive Integer idAutor) {
        public LibroAutor toEntity() {
            LibroAutor x = new LibroAutor(); Libro libro = new Libro(); libro.setIdLibro(idLibro);
            Autor autor = new Autor(); autor.setIdAutor(idAutor); x.setLibro(libro); x.setAutor(autor); return x;
        }
    }

    public record Response(Integer idLibro, Integer idAutor) {
        public static Response desde(LibroAutor x) { return new Response(x.getLibro().getIdLibro(), x.getAutor().getIdAutor()); }
    }
}
