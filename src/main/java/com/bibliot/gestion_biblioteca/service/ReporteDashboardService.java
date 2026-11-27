package com.bibliot.gestion_biblioteca.service;

import com.bibliot.gestion_biblioteca.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReporteDashboardService {
    private final LibroRepository libros;
    private final UsuarioRepository usuarios;
    private final PrestamoRepository prestamos;
    private final EjemplarRepository ejemplares;
    private final LibroCategoriaRepository libroCategorias;
    private final LibroAutorRepository libroAutores;

    public record Mes(String etiqueta, long prestamos) {}
    public record CategoriaMasSolicitada(String nombre, long prestamos) {}
    public record PrestamoReciente(String usuario, String libro, LocalDateTime fechaPrestamo,
                                   LocalDateTime fechaVencimiento, String estado) {}
    public record LibroPopular(String titulo, String autores, long prestamos) {}
    public record Resumen(long totalLibros, long totalEjemplares, long totalUsuarios, long totalLectores,
                          long prestamosActivos, long prestamosVencidos, long prestamosMes,
                          List<Mes> prestamosPorMes, List<CategoriaMasSolicitada> categoriasMasSolicitadas,
                          List<PrestamoReciente> ultimosPrestamos, List<LibroPopular> librosMasPrestados) {}

    @Transactional(readOnly = true)
    public Resumen obtenerResumen() {
        LocalDateTime ahora = LocalDateTime.now();
        YearMonth mesActual = YearMonth.from(ahora);
        YearMonth primerMes = mesActual.minusMonths(5);
        LocalDateTime inicioSerie = primerMes.atDay(1).atStartOfDay();
        LocalDateTime finSerie = mesActual.plusMonths(1).atDay(1).atStartOfDay();

        Map<YearMonth, Long> conteosPorMes = new HashMap<>();
        for (int i = 0; i < 6; i++) conteosPorMes.put(primerMes.plusMonths(i), 0L);
        for (Object[] fila : prestamos.contarPorMes(inicioSerie, finSerie)) {
            YearMonth mes = YearMonth.of(((Number) fila[0]).intValue(), ((Number) fila[1]).intValue());
            conteosPorMes.put(mes, ((Number) fila[2]).longValue());
        }
        List<Mes> serieMensual = conteosPorMes.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entrada -> new Mes(entrada.getKey().getMonth()
                        .getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("es-CO")), entrada.getValue()))
                .toList();

        List<CategoriaMasSolicitada> categorias = libroCategorias
                .categoriasPopularesDashboard(PageRequest.of(0, 5)).stream()
                .map(fila -> new CategoriaMasSolicitada((String) fila[0], ((Number) fila[1]).longValue()))
                .toList();

        List<Object[]> filasPopulares = prestamos.librosPopularesDashboard(PageRequest.of(0, 5));
        List<Integer> idsPopulares = filasPopulares.stream()
                .map(fila -> ((Number) fila[0]).intValue()).toList();
        Map<Integer, List<String>> autoresPorLibro = new HashMap<>();
        if (!idsPopulares.isEmpty()) {
            for (Object[] fila : libroAutores.autoresDeLibros(idsPopulares)) {
                autoresPorLibro.computeIfAbsent(((Number) fila[0]).intValue(), ignored -> new ArrayList<>())
                        .add((String) fila[1]);
            }
        }
        List<LibroPopular> populares = filasPopulares.stream()
                .map(fila -> {
                    Integer idLibro = ((Number) fila[0]).intValue();
                    return new LibroPopular((String) fila[1],
                            String.join(", ", autoresPorLibro.getOrDefault(idLibro, List.of())),
                            ((Number) fila[2]).longValue());
                })
                .toList();

        List<PrestamoReciente> recientes = prestamos.ultimosParaDashboard(ahora, PageRequest.of(0, 6)).stream()
                .map(fila -> new PrestamoReciente((String) fila[0], (String) fila[1],
                        (LocalDateTime) fila[2], (LocalDateTime) fila[3], (String) fila[4]))
                .toList();

        return new Resumen(libros.count(), ejemplares.count(), usuarios.count(),
                usuarios.countByRolNombreIgnoreCase("LECTOR"), prestamos.contarPendientesDashboard(),
                prestamos.contarVencidosDashboard(ahora), prestamos.contarEntreFechas(
                        mesActual.atDay(1).atStartOfDay(), finSerie),
                serieMensual, categorias, recientes, populares);
    }
}