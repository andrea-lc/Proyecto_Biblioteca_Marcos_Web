package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.AutorDtos;
import com.bibliot.gestion_biblioteca.dto.EditorialDtos;
import com.bibliot.gestion_biblioteca.dto.CatalogoAdminDtos;
import com.bibliot.gestion_biblioteca.repository.LibroAutorRepository;
import com.bibliot.gestion_biblioteca.repository.LibroRepository;
import com.bibliot.gestion_biblioteca.service.AutorService;
import com.bibliot.gestion_biblioteca.service.EditorialService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BibliotecarioPersonasCatalogoController {
    private final AutorService autores;
    private final EditorialService editoriales;
    private final LibroAutorRepository librosAutores;
    private final LibroRepository libros;

    public BibliotecarioPersonasCatalogoController(AutorService autores, EditorialService editoriales,
            LibroAutorRepository librosAutores, LibroRepository libros) {
        this.autores = autores;
        this.editoriales = editoriales;
        this.librosAutores = librosAutores;
        this.libros = libros;
    }

    @GetMapping("/api/bibliotecario/autores")
    public List<AutorDtos.Response> autores() {
        return autores.listarTodos().stream().map(AutorDtos.Response::desde).toList();
    }

    @GetMapping("/api/bibliotecario/autores/con-libros")
    public List<CatalogoAdminDtos.Autor> autoresConLibros() {
        Map<Integer, Long> conteos = librosAutores.contarLibrosPorAutor().stream()
                .collect(Collectors.toMap(row -> ((Number) row[0]).intValue(), row -> ((Number) row[1]).longValue()));
        return autores.listarTodos().stream()
                .map(autor -> new CatalogoAdminDtos.Autor(autor.getIdAutor(), autor.getNombres(),
                        autor.getApellidos(), autor.getNacionalidad(), autor.getBiografia(),
                        conteos.getOrDefault(autor.getIdAutor(), 0L)))
                .toList();
    }

    @GetMapping("/api/bibliotecario/autores/{id}")
    public AutorDtos.Response autor(@PathVariable Integer id) {
        return AutorDtos.Response.desde(autores.buscarPorId(id));
    }

    @GetMapping("/api/bibliotecario/editoriales")
    public List<EditorialDtos.Response> editoriales() {
        return editoriales.listarTodos().stream().map(EditorialDtos.Response::desde).toList();
    }

    @GetMapping("/api/bibliotecario/editoriales/con-libros")
    public List<CatalogoAdminDtos.Editorial> editorialesConLibros() {
        Map<Integer, Long> conteos = libros.contarLibrosPorEditorial().stream()
                .collect(Collectors.toMap(row -> ((Number) row[0]).intValue(), row -> ((Number) row[1]).longValue()));
        return editoriales.listarTodos().stream()
                .map(editorial -> new CatalogoAdminDtos.Editorial(editorial.getIdEditorial(), editorial.getNombre(),
                        editorial.getPais(), editorial.getSitioWeb(),
                        conteos.getOrDefault(editorial.getIdEditorial(), 0L)))
                .toList();
    }

    @GetMapping("/api/bibliotecario/editoriales/{id}")
    public EditorialDtos.Response editorial(@PathVariable Integer id) {
        return EditorialDtos.Response.desde(editoriales.buscarPorId(id));
    }
}
