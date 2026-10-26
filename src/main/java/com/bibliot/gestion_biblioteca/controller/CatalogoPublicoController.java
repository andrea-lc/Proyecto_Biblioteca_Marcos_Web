package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.entities.Libro;
import com.bibliot.gestion_biblioteca.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/catalogo")
@CrossOrigin(origins = "*")
public class CatalogoPublicoController {

    @Autowired
    private LibroService libroService;

    // Ver todos los libros
    @GetMapping("/libros")
    public ResponseEntity<List<Libro>> listarLibros() {
        return ResponseEntity.ok(libroService.listarTodos());
    }

    // Ver detalle de un libro por su ID
    @GetMapping("/libros/{id}")
    public ResponseEntity<Libro> buscarLibro(@PathVariable Integer id) {
        return ResponseEntity.ok(libroService.buscarPorId(id));
    }

    // Buscar libros por título
    @GetMapping("/buscar")
    public ResponseEntity<List<Libro>> buscarPorTitulo(@RequestParam String titulo) {
        return ResponseEntity.ok(libroService.buscarPorTitulo(titulo));
    }
}
