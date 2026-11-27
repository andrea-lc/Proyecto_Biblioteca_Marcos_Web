package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.LibroDtos.Request;
import com.bibliot.gestion_biblioteca.dto.LibroDtos.Response;
import com.bibliot.gestion_biblioteca.dto.LibroDtos.PageResponse;
import com.bibliot.gestion_biblioteca.entities.Libro;
import com.bibliot.gestion_biblioteca.entities.LibroDigital;
import com.bibliot.gestion_biblioteca.repository.EditorialRepository;
import com.bibliot.gestion_biblioteca.repository.LibroDigitalRepository;
import com.bibliot.gestion_biblioteca.service.LibroArchivoService;
import com.bibliot.gestion_biblioteca.service.LibroArchivoService.ArchivoGuardado;
import com.bibliot.gestion_biblioteca.service.LibroService;
import com.bibliot.gestion_biblioteca.service.LibroResponseAssembler;
import com.bibliot.gestion_biblioteca.service.LibroAutorService;
import com.bibliot.gestion_biblioteca.service.LibroCategoriaService;
import com.bibliot.gestion_biblioteca.service.NotificacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.time.LocalDateTime;

@RestController
@Transactional
@Validated
@RequestMapping("/api/admin/libros")
public class LibroController {
    private final LibroService service;
    private final EditorialRepository editoriales;
    private final LibroResponseAssembler assembler;
    private final LibroAutorService autores;
    private final LibroCategoriaService categorias;
    private final LibroDigitalRepository digitales;
    private final LibroArchivoService archivos;
    private final NotificacionService notificaciones;

    public LibroController(LibroService service, EditorialRepository editoriales, LibroResponseAssembler assembler,
            LibroAutorService autores, LibroCategoriaService categorias, LibroDigitalRepository digitales,
            LibroArchivoService archivos, NotificacionService notificaciones) {
        this.service = service;
        this.editoriales = editoriales;
        this.assembler = assembler;
        this.autores = autores;
        this.categorias = categorias;
        this.digitales = digitales;
        this.archivos = archivos;
        this.notificaciones = notificaciones;
    }

    @GetMapping
    public List<Response> listar(@RequestParam(required = false) String q,
            @RequestParam(required = false) String titulo) {
        String filtro = q != null ? q : titulo;
        List<Libro> libros = filtro == null || filtro.isBlank() ? service.listarTodos()
                : service.buscarPorTitulo(filtro);
        return assembler.desdeTodos(libros);
    }

    @GetMapping("/pagina")
    public PageResponse listarPagina(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) @Min(1) Integer autorId,
            @RequestParam(required = false) @Min(1) Integer editorialId,
            @RequestParam(required = false) @Min(1) Integer categoriaId,
            @RequestParam(required = false) @Size(max = 30) String estado,
            @RequestParam(required = false) @Pattern(regexp = "(?i)FISICO|DIGITAL|AMBOS|SIN_FORMATO") String formato) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "idLibro"));
        Page<Libro> resultado = service.buscarPagina(q, autorId, editorialId, categoriaId, estado, formato, pageable);
        return new PageResponse(
                assembler.desdeTodos(resultado.getContent()), resultado.getNumber(), resultado.getSize(),
                resultado.getTotalElements(), resultado.getTotalPages(), resultado.isFirst(), resultado.isLast());
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
        notificaciones.libroNuevo(libro);
        autores.reemplazarPorLibro(libro.getIdLibro(), dto.autorIds());
        categorias.reemplazarPorLibro(libro.getIdLibro(), dto.categoriaIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.desde(libro));
    }

    @PostMapping(value = "/con-archivos", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response> crearConArchivos(@Valid @RequestPart("datos") Request dto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen,
            @RequestPart(value = "archivoPdf", required = false) MultipartFile archivoPdf) {
        ArchivoGuardado portadaGuardada = null;
        ArchivoGuardado pdfGuardado = null;
        try {
            portadaGuardada = archivos.guardarImagen(imagen);
            pdfGuardado = archivos.guardarPdfYExtraerTexto(archivoPdf);

            Libro libro = map(dto, null);
            if (portadaGuardada != null) libro.setImagenUrl(portadaGuardada.url());
            if (pdfGuardado != null) libro.setTextoExtraido(pdfGuardado.textoExtraido());
            libro = service.guardar(libro);
            notificaciones.libroNuevo(libro);
            autores.reemplazarPorLibro(libro.getIdLibro(), dto.autorIds());
            categorias.reemplazarPorLibro(libro.getIdLibro(), dto.categoriaIds());

            if (pdfGuardado != null) {
                LibroDigital digital = new LibroDigital();
                digital.setLibro(libro);
                digital.setArchivoUrl(pdfGuardado.url());
                digital.setFormato("PDF");
                digital.setTamanoArchivo(pdfGuardado.tamano());
                digital.setDisponible(true);
                digital.setFechaRegistro(LocalDateTime.now());
                digitales.save(digital);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(assembler.desde(libro));
        } catch (RuntimeException ex) {
            archivos.eliminar(portadaGuardada);
            archivos.eliminar(pdfGuardado);
            throw ex;
        }
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
