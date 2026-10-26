package com.bibliot.gestion_biblioteca.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.bibliot.gestion_biblioteca.entities.Reserva;
import com.bibliot.gestion_biblioteca.repository.ReservaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public List<Reserva> listarTodos() {
        return reservaRepository.findAll();
    }

    public Reserva buscarPorId(Integer id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con ID: " + id));
    }

    @Transactional
    public Reserva guardar(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    @Transactional
    public void eliminar(Integer id) {
        Reserva reserva = buscarPorId(id);
        reservaRepository.delete(reserva);
    }

    public List<Reserva> buscarPorUsuario(Integer idUsuario) {
        return reservaRepository.findByUsuarioIdUsuario(idUsuario);
    }

    public List<Reserva> buscarPorLibro(Integer idLibro) {
        return reservaRepository.findByLibroIdLibro(idLibro);
    }
}