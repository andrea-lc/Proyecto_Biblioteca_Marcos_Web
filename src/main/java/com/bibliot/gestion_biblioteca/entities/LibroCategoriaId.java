package com.bibliot.gestion_biblioteca.entities;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class LibroCategoriaId implements Serializable {

    private Integer idLibro;
    private Integer idCategoria;

    public LibroCategoriaId(Integer idLibro, Integer idCategoria) {
        this.idLibro = idLibro;
        this.idCategoria = idCategoria;
    }
}