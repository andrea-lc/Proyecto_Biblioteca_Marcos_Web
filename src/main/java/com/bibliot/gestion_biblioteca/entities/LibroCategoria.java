package com.bibliot.gestion_biblioteca.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Tabla intermedia que resuelve la relación N:M entre Libro y Categoria.
@Entity
@Table(name = "libro_categoria")
@Getter
@Setter
@NoArgsConstructor
public class LibroCategoria {

    @EmbeddedId
    private LibroCategoriaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idLibro")
    @JoinColumn(name = "id_libro")
    private Libro libro;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idCategoria")
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    public LibroCategoria(Libro libro, Categoria categoria) {
        this.libro = libro;
        this.categoria = categoria;
        this.id = new LibroCategoriaId(libro.getIdLibro(), categoria.getIdCategoria());
    }
}