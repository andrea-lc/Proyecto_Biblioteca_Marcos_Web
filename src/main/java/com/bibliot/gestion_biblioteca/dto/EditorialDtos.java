package com.bibliot.gestion_biblioteca.dto;

import com.bibliot.gestion_biblioteca.entities.Editorial;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public final class EditorialDtos {
    private EditorialDtos() {}

    public record Request(Integer idEditorial, @NotBlank @Size(max = 150) String nombre,
            @Size(max = 100) String pais, @URL String sitioWeb) {
        public Editorial toEntity() { Editorial x = new Editorial(); x.setIdEditorial(idEditorial); x.setNombre(nombre); x.setPais(pais); x.setSitioWeb(sitioWeb); return x; }
    }

    public record Response(Integer idEditorial, String nombre, String pais, String sitioWeb) {
        public static Response desde(Editorial x) { return new Response(x.getIdEditorial(), x.getNombre(), x.getPais(), x.getSitioWeb()); }
    }
}
