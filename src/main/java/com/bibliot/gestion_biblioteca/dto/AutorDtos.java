package com.bibliot.gestion_biblioteca.dto;

import com.bibliot.gestion_biblioteca.entities.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class AutorDtos {
    private AutorDtos() {}

    public record Request(Integer idAutor, @NotBlank @Size(max = 100) String nombres,
            @NotBlank @Size(max = 100) String apellidos, @Size(max = 100) String nacionalidad,
            @Size(max = 5000) String biografia) {
        public Autor toEntity() { Autor x = new Autor(); x.setIdAutor(idAutor); x.setNombres(nombres); x.setApellidos(apellidos); x.setNacionalidad(nacionalidad); x.setBiografia(biografia); return x; }
    }

    public record Response(Integer idAutor, String nombres, String apellidos, String nacionalidad, String biografia) {
        public static Response desde(Autor x) { return new Response(x.getIdAutor(), x.getNombres(), x.getApellidos(), x.getNacionalidad(), x.getBiografia()); }
    }
}
