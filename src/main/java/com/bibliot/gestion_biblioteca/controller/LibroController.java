package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.LibroDtos.Request;
import com.bibliot.gestion_biblioteca.dto.LibroDtos.Response;
import com.bibliot.gestion_biblioteca.entities.Libro;
import com.bibliot.gestion_biblioteca.repository.EditorialRepository;
import com.bibliot.gestion_biblioteca.service.LibroService;
import com.bibliot.gestion_biblioteca.service.LibroResponseAssembler;
import com.bibliot.gestion_biblioteca.service.LibroAutorService;
import com.bibliot.gestion_biblioteca.service.LibroCategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/api/admin/libros")
public class LibroController {
    private final LibroService service;
    private final EditorialRepository editoriales;
    private final LibroResponseAssembler assembler;
    private final LibroAutorService autores;
    private final LibroCategoriaService categorias;

    public LibroController(LibroService service, EditorialRepository editoriales, LibroResponseAssembler assembler,
            LibroAutorService autores, LibroCategoriaService categorias) {
        this.service = service;
        this.editoriales = editoriales;
        this.assembler = assembler;
        this.autores = autores;
        this.categorias = categorias;
    }

    @GetMapping
    public List<Response> listar(@RequestParam(required = false) String q,
            @RequestParam(required = false) String titulo) {
        String filtro = q != null ? q : titulo;
        List<Libro> libros = filtro == null || filtro.isBlank() ? service.listarTodos()
                : service.buscarPorTitulo(filtro);
        return assembler.desdeTodos(libros);
    }

    @GetMapping("/{id}")
    public Response buscar(@PathVariable Integer id) {
        return assembler.desde(service.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public List<Response> buscarTitulo(@RequestParam String titulo) {
        return assembler.desdeTodos(service.buscarPorTitulo(titulo));
    }

    @GetMapping("/editorial/{idEditorial}")
    public List<Response> porEditorial(@PathVariable Integer idEditorial) {
        return assembler.desdeTodos(service.buscarPorEditorial(idEditorial));
    }

    @PostMapping
    public ResponseEntity<Response> crear(@Valid @RequestBody Request dto) {
        Libro libro = service.guardar(map(dto, null));
        autores.reemplazarPorLibro(libro.getIdLibro(), dto.autorIds());
        categorias.reemplazarPorLibro(libro.getIdLibro(), dto.categoriaIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.desde(libro));
    }

    @PutMapping("/{id}")
    public Response actualizar(@PathVariable Integer id, @Valid @RequestBody Request dto) {
        Libro libro = service.guardar(map(dto, id));
        if (dto.autorIds() != null) autores.reemplazarPorLibro(id, dto.autorIds());
        if (dto.categoriaIds() != null) categorias.reemplazarPorLibro(id, dto.categoriaIds());
        return assembler.desde(libro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        autores.reemplazarPorLibro(id, List.of());
        categorias.reemplazarPorLibro(id, List.of());
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private Libro map(Request d, Integer id) {
        Libro x = id == null ? new Libro() : service.buscarPorId(id);
        x.setTitulo(d.titulo());
        x.setIsbn(d.isbn());
        x.setPaginas(d.paginas());
        x.setFechaPublicacion(d.fechaPublicacion());
        x.setEstado(d.estado());
        x.setDescripcion(d.descripcion());
        x.setContenidoTexto(d.contenidoTexto());
        x.setEditorial(d.idEditorial() == null ? null
                : editoriales.findById(d.idEditorial()).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Editorial no encontrada")));
        return x;
    }
}
