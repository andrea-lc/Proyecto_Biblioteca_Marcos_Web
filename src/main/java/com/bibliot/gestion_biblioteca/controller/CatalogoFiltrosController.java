package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.CatalogoFiltrosDtos.Opcion;
import com.bibliot.gestion_biblioteca.dto.CatalogoFiltrosDtos.Response;
import com.bibliot.gestion_biblioteca.service.AutorService;
import com.bibliot.gestion_biblioteca.service.CategoriaService;
import com.bibliot.gestion_biblioteca.service.EditorialService;
import com.bibliot.gestion_biblioteca.repository.LibroRepository;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(readOnly = true)
@RequestMapping("/api/catalogo/filtros")
public class CatalogoFiltrosController {
    private final AutorService autores;
    private final CategoriaService categorias;
    private final EditorialService editoriales;
    private final LibroRepository libros;

    public CatalogoFiltrosController(AutorService autores, CategoriaService categorias,
            EditorialService editoriales, LibroRepository libros) {
        this.autores = autores;
        this.categorias = categorias;
        this.editoriales = editoriales;
        this.libros = libros;
    }

    @GetMapping
    public Response listar() {
        List<Opcion> opcionesAutores = autores.listarTodos().stream()
                .map(autor -> new Opcion(autor.getIdAutor(),
                        (autor.getNombres() + " " + autor.getApellidos()).trim()))
                .toList();
        List<Opcion> opcionesCategorias = categorias.listarTodos().stream()
                .map(categoria -> new Opcion(categoria.getIdCategoria(), categoria.getNombre()))
                .toList();
        List<Opcion> opcionesEditoriales = editoriales.listarTodos().stream()
                .map(editorial -> new Opcion(editorial.getIdEditorial(), editorial.getNombre()))
                .toList();
        return new Response(opcionesAutores, opcionesCategorias, opcionesEditoriales, libros.findEstadosDistintos());
    }
}
