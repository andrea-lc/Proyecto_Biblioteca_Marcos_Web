package com.bibliot.gestion_biblioteca.service;

import com.bibliot.gestion_biblioteca.dto.LibroResponse;
import com.bibliot.gestion_biblioteca.entities.Libro;
import com.bibliot.gestion_biblioteca.repository.LibroAutorRepository;
import com.bibliot.gestion_biblioteca.repository.LibroCategoriaRepository;
import com.bibliot.gestion_biblioteca.repository.LibroDigitalRepository;
import com.bibliot.gestion_biblioteca.repository.EjemplarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class LibroResponseAssembler {
    private final LibroAutorRepository autores;
    private final LibroCategoriaRepository categorias;
    private final LibroDigitalRepository digitales;
    private final EjemplarRepository ejemplares;

    public LibroResponseAssembler(LibroAutorRepository autores, LibroCategoriaRepository categorias,
            LibroDigitalRepository digitales, EjemplarRepository ejemplares) {
        this.autores = autores;
        this.categorias = categorias;
        this.digitales = digitales;
        this.ejemplares = ejemplares;
    }

    public LibroResponse desde(Libro libro) {
        var relacionesAutor = autores.findByLibroIdLibro(libro.getIdLibro());
        var relacionesCategoria = categorias.findByLibroIdLibro(libro.getIdLibro());
        List<String> nombresAutores = relacionesAutor.stream()
                .map(a -> (a.getAutor().getNombres() + " " + a.getAutor().getApellidos()).trim()).toList();
        List<Integer> idsAutores = relacionesAutor.stream().map(a -> a.getAutor().getIdAutor()).toList();
        List<String> nombresCategorias = relacionesCategoria.stream().map(c -> c.getCategoria().getNombre()).toList();
        List<Integer> idsCategorias = relacionesCategoria.stream().map(c -> c.getCategoria().getIdCategoria()).toList();
        boolean tieneDigital = digitales.findByLibroIdLibro(libro.getIdLibro()).isPresent();
        boolean tieneFisico = !ejemplares.findByLibroIdLibro(libro.getIdLibro()).isEmpty();
        List<String> formatos = new ArrayList<>();
        if (tieneFisico) formatos.add("Físico");
        if (tieneDigital) formatos.add("Digital");
        LibroResponse base = LibroResponse.desde(libro);
        return new LibroResponse(base.idLibro(), base.titulo(), base.isbn(), base.paginas(), base.fechaPublicacion(),
                base.estado(), base.descripcion(), base.contenidoTexto(), base.idEditorial(), base.editorial(),
                nombresAutores, idsAutores, nombresCategorias, idsCategorias,
                formatos.isEmpty() ? "Sin ejemplares" : String.join(" / ", formatos), ejemplares.findByLibroIdLibro(libro.getIdLibro()).size());
    }

    public List<LibroResponse> desdeTodos(List<Libro> libros) {
        return libros.stream().map(this::desde).toList();
    }
}
