package com.bibliot.gestion_biblioteca.dto;

import java.util.List;

public final class CatalogoFiltrosDtos {
    private CatalogoFiltrosDtos() {}

    public record Opcion(Integer id, String nombre) {}

    public record Response(List<Opcion> autores, List<Opcion> categorias, List<Opcion> editoriales,
            List<String> estados) {}
}
