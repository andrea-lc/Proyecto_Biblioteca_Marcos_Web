package com.bibliot.gestion_biblioteca.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

// Clave compuesta de la tabla intermedia LIBRO_AUTOR.
// Debe implementar Serializable y equals/hashCode: es un requisito de JPA, no un extra.
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class LibroAutorId implements Serializable {

    @Column(name = "id_libro")
    private Integer idLibro;
    @Column(name = "id_autor")
    private Integer idAutor;

    public LibroAutorId(Integer idLibro, Integer idAutor) {
        this.idLibro = idLibro;
        this.idAutor = idAutor;
    }
}
