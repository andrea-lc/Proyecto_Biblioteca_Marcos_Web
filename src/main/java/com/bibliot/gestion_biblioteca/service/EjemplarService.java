package com.bibliot.gestion_biblioteca.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bibliot.gestion_biblioteca.entities.Ejemplar;
import com.bibliot.gestion_biblioteca.repository.EjemplarRepository;
import com.bibliot.gestion_biblioteca.repository.PrestamoRepository;
import jakarta.transaction.Transactional;

@Service
public class EjemplarService {
    @Autowired
    private EjemplarRepository ejemplarRepository;

    @Autowired
    private PrestamoRepository prestamoRepository;

    public List<Ejemplar> listarTodos() {
        return ejemplarRepository.findAllConLibro();
    }

    public Ejemplar buscarPorId(Integer id) {
        return ejemplarRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ejemplar no encontrado con ID: " + id));
    }

    @Transactional
    public Ejemplar guardar(Ejemplar ejemplar) {
        if (ejemplar.getCodigoInventario() != null) {
            ejemplar.setCodigoInventario(ejemplar.getCodigoInventario().trim());
        }
        if (ejemplar.getUbicacion() != null) {
            ejemplar.setUbicacion(ejemplar.getUbicacion().trim());
        }
        if (ejemplar.getEstado() != null) {
            ejemplar.setEstado(ejemplar.getEstado().trim());
        }
        // Validar código de inventario único
        if (ejemplar.getCodigoInventario() != null && !ejemplar.getCodigoInventario().isEmpty()) {
            Optional<Ejemplar> existente = ejemplarRepository.findByCodigoInventario(ejemplar.getCodigoInventario());

            // Si existe Y (es un nuevo ejemplar O el ID no coincide)
            if (existente.isPresent() && (ejemplar.getIdEjemplar() == null ||
                    !existente.get().getIdEjemplar().equals(ejemplar.getIdEjemplar()))) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "El código de inventario '" + ejemplar.getCodigoInventario() + "' ya está registrado.");
            }
        }
        return ejemplarRepository.save(ejemplar);
    }

    @Transactional
    public Ejemplar actualizar(Integer id, Ejemplar datosActualizados) {
        Ejemplar existente = buscarPorId(id);

        if (datosActualizados.getCodigoInventario() != null) {
            existente.setCodigoInventario(datosActualizados.getCodigoInventario().trim());
        }
        if (datosActualizados.getUbicacion() != null) {
            existente.setUbicacion(datosActualizados.getUbicacion().trim());
        }
        if (datosActualizados.getEstado() != null) {
            existente.setEstado(datosActualizados.getEstado().trim());
        }
        if (datosActualizados.getFechaAdquisicion() != null) {
            existente.setFechaAdquisicion(datosActualizados.getFechaAdquisicion());
        }

        return guardar(existente);
    }

    @Transactional
    public void eliminar(Integer id) {
        Ejemplar ejemplar = buscarPorId(id);
        String est = ejemplar.getEstado() == null ? "" : ejemplar.getEstado().trim().toLowerCase();
        if (est.equals("prestado") || est.equals("reservado")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede eliminar un ejemplar en estado '" + ejemplar.getEstado() + "'.");
        }
        if (!prestamoRepository.findByEjemplarIdEjemplar(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede eliminar este ejemplar porque tiene préstamos registrados en el historial.");
        }
        ejemplarRepository.delete(ejemplar);
    }

    public Ejemplar buscarPorCodigoInventario(String codigo) {
        return ejemplarRepository.findByCodigoInventario(codigo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ejemplar no encontrado con código: " + codigo));
    }

    public List<Ejemplar> buscarPorLibro(Integer idLibro) {
        return ejemplarRepository.findByLibroIdLibro(idLibro);
    }
}
