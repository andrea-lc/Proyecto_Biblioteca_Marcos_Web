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

// Tabla intermedia que resuelve la relación N:M entre Libro y Autor.
@Entity
@Table(name = "libro_autor")
@Getter
@Setter
@NoArgsConstructor
public class LibroAutor {

    @EmbeddedId
    private LibroAutorId id;

    // @MapsId conecta este campo con la parte "idLibro" del id compuesto.
    // Es necesario: sin esto, JPA no sabe de dónde sacar esa parte de la clave.
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idLibro")
    @JoinColumn(name = "id_libro")
    private Libro libro;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idAutor")
    @JoinColumn(name = "id_autor")
    private Autor autor;

    public LibroAutor(Libro libro, Autor autor) {
        this.libro = libro;
        this.autor = autor;
        this.id = new LibroAutorId(libro.getIdLibro(), autor.getIdAutor());
    }
}