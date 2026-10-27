package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.entities.*;
import com.bibliot.gestion_biblioteca.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdministradorController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RolService rolService;
    @Autowired
    private LibroService libroService;
    @Autowired
    private LibroDigitalService libroDigitalService;
    @Autowired
    private AutorService autorService;
    @Autowired
    private EditorialService editorialService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private EjemplarService ejemplarService;

    // Gestion de usuarios
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PostMapping("/usuarios")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.crearUsuario(usuario));
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Integer id, @RequestBody Usuario detalles) {
        return ResponseEntity.ok(usuarioService.actualizar(id, detalles));
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return ResponseEntity.ok().body("Usuario eliminado correctamente");
    }

    @GetMapping("/usuarios/email/{email}")
    public ResponseEntity<Usuario> buscarPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(usuarioService.buscarPorEmail(email));
    }

    @GetMapping("/usuarios/dni/{dni}")
    public ResponseEntity<Usuario> buscarPorDni(@PathVariable String dni) {
        return ResponseEntity.ok(usuarioService.buscarPorDni(dni));
    }

    // Gestion de roles
    @GetMapping("/roles")
    public ResponseEntity<List<Rol>> listarRoles() {
        return ResponseEntity.ok(rolService.listarTodos());
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Rol> buscarRolPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(rolService.buscarPorId(id));
    }

    @PostMapping("/roles")
    public ResponseEntity<Rol> crearRol(@RequestBody Rol rol) {
        return ResponseEntity.ok(rolService.guardar(rol));
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<Rol> actualizarRol(@PathVariable Integer id, @RequestBody Rol detalles) {
        return ResponseEntity.ok(rolService.actualizar(id, detalles));
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<?> eliminarRol(@PathVariable Integer id) {
        rolService.eliminar(id);
        return ResponseEntity.ok().body("Rol eliminado correctamente");
    }

    @GetMapping("/roles/nombre/{nombre}")
    public ResponseEntity<Rol> buscarRolPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(rolService.buscarPorNombre(nombre));
    }

    // Gestion de libros
    @GetMapping("/libros")
    public ResponseEntity<List<Libro>> listarLibros() {
        return ResponseEntity.ok(libroService.listarTodos());
    }

    @GetMapping("/libros/{id}")
    public ResponseEntity<Libro> buscarLibroPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(libroService.buscarPorId(id));
    }

    @PostMapping("/libros")
    public ResponseEntity<Libro> crearLibro(@RequestBody Libro libro) {
        return ResponseEntity.ok(libroService.guardar(libro));
    }

    @PutMapping("/libros/{id}")
    public ResponseEntity<Libro> actualizarLibro(@PathVariable Integer id, @RequestBody Libro detalles) {
        return ResponseEntity.ok(libroService.actualizar(id, detalles));
    }

    @DeleteMapping("/libros/{id}")
    public ResponseEntity<?> eliminarLibro(@PathVariable Integer id) {
        libroService.eliminar(id);
        return ResponseEntity.ok().body("Libro eliminado correctamente");
    }

    @GetMapping("/libros/buscar")
    public ResponseEntity<List<Libro>> buscarPorTitulo(@RequestParam String titulo) {
        return ResponseEntity.ok(libroService.buscarPorTitulo(titulo));
    }

    @GetMapping("/libros/editorial/{idEditorial}")
    public ResponseEntity<List<Libro>> buscarPorEditorial(@PathVariable Integer idEditorial) {
        return ResponseEntity.ok(libroService.buscarPorEditorial(idEditorial));
    }

    // Gestion de libros digitales
    @GetMapping("/libros-digitales")
    public ResponseEntity<List<LibroDigital>> listarLibrosDigitales() {
        return ResponseEntity.ok(libroDigitalService.listarTodos());
    }

    @GetMapping("/libros-digitales/{id}")
    public ResponseEntity<LibroDigital> buscarLibroDigitalPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(libroDigitalService.buscarPorId(id));
    }

    @PostMapping("/libros-digitales")
    public ResponseEntity<LibroDigital> crearLibroDigital(@RequestBody LibroDigital libroDigital) {
        return ResponseEntity.ok(libroDigitalService.guardar(libroDigital));
    }

    @PutMapping("/libros-digitales/{id}")
    public ResponseEntity<LibroDigital> actualizarLibroDigital(@PathVariable Integer id,
            @RequestBody LibroDigital detalles) {

        return ResponseEntity.ok(libroDigitalService.actualizar(id,detalles ));

    }

    @DeleteMapping("/libros-digitales/{id}")
    public ResponseEntity<?> eliminarLibroDigital(@PathVariable Integer id) {
        libroDigitalService.eliminar(id);
        return ResponseEntity.ok().body("Libro digital eliminado correctamente");
    }

    @GetMapping("/libros-digitales/libro/{idLibro}")
    public ResponseEntity<LibroDigital> buscarPorLibro(@PathVariable Integer idLibro) {
        return ResponseEntity.ok(libroDigitalService.buscarPorId(idLibro));
    }

    // Gestion de autores
    @GetMapping("/autores")
    public ResponseEntity<List<Autor>> listarAutores() {
        return ResponseEntity.ok(autorService.listarTodos());
    }

    @GetMapping("/autores/{id}")
    public ResponseEntity<Autor> buscarAutorPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(autorService.buscarPorId(id));
    }

    @PostMapping("/autores")
    public ResponseEntity<Autor> crearAutor(@RequestBody Autor autor) {
        return ResponseEntity.ok(autorService.guardar(autor));
    }

    @PutMapping("/autores/{id}")
    public ResponseEntity<Autor> actualizarAutor(@PathVariable Integer id, @RequestBody Autor detalles) {
        return ResponseEntity.ok(autorService.actualizar(id, detalles));
    }

    @DeleteMapping("/autores/{id}")
    public ResponseEntity<?> eliminarAutor(@PathVariable Integer id) {
        autorService.eliminar(id);
        return ResponseEntity.ok().body("Autor eliminado correctamente");
    }

    @GetMapping("/autores/buscar")
    public ResponseEntity<List<Autor>> buscarAutorPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(autorService.buscarPorNombre(nombre));
    }

    // Gestion de editoriales
    @GetMapping("/editoriales")
    public ResponseEntity<List<Editorial>> listarEditoriales() {
        return ResponseEntity.ok(editorialService.listarTodos());
    }

    @GetMapping("/editoriales/{id}")
    public ResponseEntity<Editorial> buscarEditorialPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(editorialService.buscarPorId(id));
    }

    @PostMapping("/editoriales")
    public ResponseEntity<Editorial> crearEditorial(@RequestBody Editorial editorial) {
        return ResponseEntity.ok(editorialService.guardar(editorial));
    }

    @PutMapping("/editoriales/{id}")
    public ResponseEntity<Editorial> actualizarEditorial(@PathVariable Integer id, @RequestBody Editorial detalles) {
        return ResponseEntity.ok(editorialService.actualizar(id, detalles));
    }

    @DeleteMapping("/editoriales/{id}")
    public ResponseEntity<?> eliminarEditorial(@PathVariable Integer id) {
        editorialService.eliminar(id);
        return ResponseEntity.ok().body("Editorial eliminada correctamente");
    }

    // Gestion de categorias
    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.listarTodos());
    }

    @GetMapping("/categorias/{id}")
    public ResponseEntity<Categoria> buscarCategoriaPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @PostMapping("/categorias")
    public ResponseEntity<Categoria> crearCategoria(@RequestBody Categoria categoria) {
        return ResponseEntity.ok(categoriaService.guardar(categoria));
    }

    @PutMapping("/categorias/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Integer id, @RequestBody Categoria detalles) {

        return ResponseEntity.ok(categoriaService.actualizar(id, detalles));
    }

    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Integer id) {
        categoriaService.eliminar(id);
        return ResponseEntity.ok().body("Categoría eliminada correctamente");
    }

    // Gestion de ejemplares
    @GetMapping("/ejemplares")
    public ResponseEntity<List<Ejemplar>> listarEjemplares() {
        return ResponseEntity.ok(ejemplarService.listarTodos());
    }

    @GetMapping("/ejemplares/{id}")
    public ResponseEntity<Ejemplar> buscarEjemplarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(ejemplarService.buscarPorId(id));
    }

    @PostMapping("/ejemplares")
    public ResponseEntity<Ejemplar> crearEjemplar(@RequestBody Ejemplar ejemplar) {
        return ResponseEntity.ok(ejemplarService.guardar(ejemplar));
    }

    @PutMapping("/ejemplares/{id}")
    public ResponseEntity<Ejemplar> actualizarEjemplar(@PathVariable Integer id, @RequestBody Ejemplar detalles) {
        detalles.setIdEjemplar(id);
        return ResponseEntity.ok(ejemplarService.actualizar(id, detalles));
    }

    @DeleteMapping("/ejemplares/{id}")
    public ResponseEntity<?> eliminarEjemplar(@PathVariable Integer id) {
        ejemplarService.eliminar(id);
        return ResponseEntity.ok().body("Ejemplar eliminado correctamente");
    }

    @GetMapping("/ejemplares/libro/{idLibro}")
    public ResponseEntity<Ejemplar> buscarEjemplaresPorLibro(@PathVariable Integer idLibro) {
        return ResponseEntity.ok(ejemplarService.buscarPorId(idLibro));
    }

    @GetMapping("/ejemplares/codigo/{codigo}")
    public ResponseEntity<Ejemplar> buscarPorCodigoInventario(@PathVariable String codigo) {
        return ResponseEntity.ok(ejemplarService.buscarPorCodigoInventario(codigo));
    }
}