package com.bibliot.gestion_biblioteca.dto;

public final class CatalogoAdminDtos {
    private CatalogoAdminDtos() {}

    public record Autor(Integer idAutor, String nombres, String apellidos, String nacionalidad,
            String biografia, long librosAsociados) {}

    public record Editorial(Integer idEditorial, String nombre, String pais, String sitioWeb,
            long librosAsociados) {}
}
