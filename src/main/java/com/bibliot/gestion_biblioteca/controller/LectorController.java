package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.entities.Prestamo;
import com.bibliot.gestion_biblioteca.entities.Reserva;
import com.bibliot.gestion_biblioteca.entities.Usuario;
import com.bibliot.gestion_biblioteca.repository.PrestamoRepository;
import com.bibliot.gestion_biblioteca.service.PrestamoService;
import com.bibliot.gestion_biblioteca.service.ReservaService;
import com.bibliot.gestion_biblioteca.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lector")
@CrossOrigin(origins = "*")
public class LectorController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private ReservaService  reservaService;
    // Ver perfil
    @GetMapping("/perfil/{idUsuario}")
    public ResponseEntity<Usuario> verMiPerfil(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(usuarioService.buscarPorId(idUsuario));
    }

    // Actualizar perfil
    @PutMapping("/perfil/{idUsuario}")
    public ResponseEntity<Usuario> actualizarMiPerfil(@PathVariable Integer idUsuario,
            @RequestBody Usuario usuarioDetalles) {
        usuarioDetalles.setIdUsuario(idUsuario); // Asegura que no modifique a otro
        return ResponseEntity.ok(usuarioService.actualizarUsuario(usuarioDetalles));
    }

    // Ver préstamos activos
    @GetMapping("/mis-prestamos/{idUsuario}")
    public ResponseEntity<Prestamo> verMisPrestamos(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(prestamoService.buscarPorId(idUsuario));
    }

    // Solicitar un nuevo préstamo
    @PostMapping("/solicitar-prestamo")
    public ResponseEntity<Prestamo> solicitarPrestamo(@RequestBody Prestamo nuevoPrestamo) {
        return ResponseEntity.ok(prestamoService.guardar(nuevoPrestamo));
    }

    @GetMapping("/mis-reservas/{idUsuario}")
    public ResponseEntity<List<Reserva>> verMisReservas(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(reservaService.buscarPorUsuario(idUsuario));
    }

    @PostMapping("/reservar-libro")
    public ResponseEntity<Reserva> crearReserva(@RequestBody Reserva nuevaReserva) {
        return ResponseEntity.ok(reservaService.guardar(nuevaReserva));
    }

    @DeleteMapping("/reservas/{idReserva}")
    public ResponseEntity<?> cancelarReserva(@PathVariable Integer idReserva) {
        reservaService.eliminar(idReserva);
        return ResponseEntity.ok().body("Reserva cancelada correctamente");
    }

}