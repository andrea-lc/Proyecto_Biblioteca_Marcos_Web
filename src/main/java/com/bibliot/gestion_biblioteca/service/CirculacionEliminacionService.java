package com.bibliot.gestion_biblioteca.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bibliot.gestion_biblioteca.entities.Prestamo;
import com.bibliot.gestion_biblioteca.repository.DevolucionRepository;
import com.bibliot.gestion_biblioteca.repository.EjemplarRepository;
import com.bibliot.gestion_biblioteca.repository.PrestamoRepository;
import com.bibliot.gestion_biblioteca.repository.SancionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CirculacionEliminacionService {
    private final PrestamoRepository prestamos;
    private final DevolucionRepository devoluciones;
    private final SancionRepository sanciones;
    private final EjemplarRepository ejemplares;

    @Transactional
    public void eliminarPrestamos(List<Prestamo> registros) {
        java.util.Set<Integer> ejemplarIds = new java.util.HashSet<>();
        for (Prestamo prestamo : registros) {
            if (prestamo.getEjemplar() != null) ejemplarIds.add(prestamo.getEjemplar().getIdEjemplar());
            devoluciones.findByPrestamoIdPrestamo(prestamo.getIdPrestamo()).ifPresent(devolucion -> {
                sanciones.findByDevolucionIdDevolucion(devolucion.getIdDevolucion())
                        .ifPresent(sanciones::delete);
                devoluciones.delete(devolucion);
            });
        }
        prestamos.deleteAll(registros);
        for (Integer idEjemplar : ejemplarIds) {
            if (!prestamos.existsByEjemplarIdEjemplar(idEjemplar)) {
                ejemplares.findById(idEjemplar).ifPresent(ejemplar -> {
                    ejemplar.setEstado("Disponible");
                    ejemplares.save(ejemplar);
                });
            }
        }
    }
}
