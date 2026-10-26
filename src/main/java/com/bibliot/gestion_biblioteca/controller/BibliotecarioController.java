package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.entities.*;
import com.bibliot.gestion_biblioteca.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bibliotecario")
@CrossOrigin(origins = "*")
public class BibliotecarioController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private LibroService libroService;
    @Autowired
    private EjemplarService ejemplarService;
    @Autowired
    private PrestamoService prestamoService;
    @Autowired
    private DevolucionService devolucionService;
    @Autowired
    private ReservaService reservaService;
    @Autowired
    private SancionService sancionService;

    //Gestion de lectores
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
        detalles.setIdUsuario(id);
        return ResponseEntity.ok(usuarioService.actualizarUsuario(detalles));
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return ResponseEntity.ok().body("Usuario eliminado correctamente");
    }

    @GetMapping("/usuarios/email/{email}")
    public ResponseEntity<Usuario> buscarUsuarioPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(usuarioService.buscarPorEmail(email));
    }

    //Gestion de libros
    @GetMapping("/libros")
    public ResponseEntity<List<Libro>> listarLibros() {
        return ResponseEntity.ok(libroService.listarTodos());
    }

    @GetMapping("/libros/{id}")
    public ResponseEntity<Libro> buscarLibroPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(libroService.buscarPorId(id));
    }

     @GetMapping("/libros/buscar")
    public ResponseEntity<List<Libro>> buscarLibrosPorTitulo(@RequestParam String titulo) {
        return ResponseEntity.ok(libroService.buscarPorTitulo(titulo));
    }

    //Gestion de ejemplares
    @GetMapping("/ejemplares")
    public ResponseEntity<List<Ejemplar>> listarEjemplares() {
        return ResponseEntity.ok(ejemplarService.listarTodos());
    }

    @GetMapping("/ejemplares/codigo/{codigo}")
    public ResponseEntity<Ejemplar> buscarEjemplarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(ejemplarService.buscarPorCodigoInventario(codigo));
    }

    //Gestio de prestamos
    @GetMapping("/prestamos")
    public ResponseEntity<List<Prestamo>> listarPrestamos() {
        return ResponseEntity.ok(prestamoService.listarTodos());
    }

    @GetMapping("/prestamos/activos")
    public ResponseEntity<List<Prestamo>> listarPrestamosActivos() {
        return ResponseEntity.ok(prestamoService.buscarPrestamosActivos());
    }

    @GetMapping("/prestamos/{id}")
    public ResponseEntity<Prestamo> buscarPrestamoPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(prestamoService.buscarPorId(id));
    }

    @PostMapping("/prestamos")
    public ResponseEntity<Prestamo> registrarPrestamo(@RequestBody Prestamo prestamo) {
        return ResponseEntity.ok(prestamoService.guardar(prestamo));
    }

    @PutMapping("/prestamos/{id}")
    public ResponseEntity<Prestamo> actualizarPrestamo(@PathVariable Integer id, @RequestBody Prestamo detalles) {
        detalles.setIdPrestamo(id);
        return ResponseEntity.ok(prestamoService.guardar(detalles));
    }

    @DeleteMapping("/prestamos/{id}")
    public ResponseEntity<?> eliminarPrestamo(@PathVariable Integer id) {
        prestamoService.eliminar(id);
        return ResponseEntity.ok().body("Préstamo eliminado correctamente");
    }

    // Gestion de devoluciones
    @GetMapping("/devoluciones")
    public ResponseEntity<List<Devolucion>> listarDevoluciones() {
        return ResponseEntity.ok(devolucionService.listarTodos());
    }

    @GetMapping("/devoluciones/prestamo/{idPrestamo}")
    public ResponseEntity<Devolucion> buscarDevolucionPorPrestamo(@PathVariable Integer idPrestamo) {
        return ResponseEntity.ok(devolucionService.buscarPorId(idPrestamo));
    }

    @PostMapping("/devoluciones")
    public ResponseEntity<Devolucion> registrarDevolucion(@RequestBody Devolucion devolucion) {
        return ResponseEntity.ok(devolucionService.guardar(devolucion));
    }

    @DeleteMapping("/devoluciones/{id}")
    public ResponseEntity<?> eliminarDevolucion(@PathVariable Integer id) {
        devolucionService.eliminar(id);
        return ResponseEntity.ok().body("Registro de devolución eliminado");
    }

    // Gestion de reservas
    @GetMapping("/reservas")
    public ResponseEntity<List<Reserva>> listarReservas() {
        return ResponseEntity.ok(reservaService.listarTodos());
    }

    @GetMapping("/reservas/libro/{idLibro}")
    public ResponseEntity<List<Reserva>> buscarReservasPorLibro(@PathVariable Integer idLibro) {
        return ResponseEntity.ok(reservaService.buscarPorLibro(idLibro));
    }

    @PostMapping("/reservas")
    public ResponseEntity<Reserva> crearReserva(@RequestBody Reserva reserva) {
        return ResponseEntity.ok(reservaService.guardar(reserva));
    }

    @DeleteMapping("/reservas/{id}")
    public ResponseEntity<?> eliminarReserva(@PathVariable Integer id) {
        reservaService.eliminar(id);
        return ResponseEntity.ok().body("Reserva eliminada");
    }

    // Gestion de sanciones
    @GetMapping("/sanciones")
    public ResponseEntity<List<Sancion>> listarSanciones() {
        return ResponseEntity.ok(sancionService.listarTodos());
    }

    @GetMapping("/sanciones/{idLibro}")
    public ResponseEntity<Sancion> buscarPorId(@PathVariable Integer idLibro) {
        return ResponseEntity.ok(sancionService.buscarPorId(idLibro));
    }

    @PostMapping("/sanciones")
    public ResponseEntity<Sancion> crearSancion(@RequestBody Sancion sancion) {
        return ResponseEntity.ok(sancionService.guardar(sancion));
    }

    @PutMapping("/sanciones/{id}")
    public ResponseEntity<Sancion> actualizarSancion(@PathVariable Integer id, @RequestBody Sancion detalles) {
        detalles.setIdSancion(id);
        return ResponseEntity.ok(sancionService.guardar(detalles));
    }
}