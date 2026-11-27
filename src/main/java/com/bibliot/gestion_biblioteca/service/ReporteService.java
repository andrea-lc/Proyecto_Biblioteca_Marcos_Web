package com.bibliot.gestion_biblioteca.service;

import com.bibliot.gestion_biblioteca.entities.Libro;
import com.bibliot.gestion_biblioteca.entities.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReporteService {

    private static final DateTimeFormatter FECHA_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FECHA_HORA_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final LibroService libroService;
    private final UsuarioService usuarioService;

    public ReporteService(LibroService libroService, UsuarioService usuarioService) {
        this.libroService = libroService;
        this.usuarioService = usuarioService;
    }

    // --- Reportes de Libros ---

    public byte[] generarReporteLibrosPdf() {
        return exportarPdf(prepararReporteLibros());
    }

    public byte[] generarReporteLibrosExcel() {
        return exportarExcel(prepararReporteLibros(), "Libros");
    }

    private JasperPrint prepararReporteLibros() {
        List<LibroReporteItem> items = libroService.listarTodos().stream()
                .map(this::mapearLibro)
                .toList();

        Map<String, Object> params = new HashMap<>();
        params.put("fechaGeneracion", LocalDateTime.now().format(FECHA_HORA_FMT));
        params.put("totalLibros", items.size());

        return llenarReporte("reports/reporte_libros.jrxml", params, items);
    }

    // --- Reportes de Usuarios ---

    @Transactional(readOnly = true)
    public byte[] generarReporteUsuariosPdf() {
        return exportarPdf(prepararReporteUsuarios());
    }

    @Transactional(readOnly = true)
    public byte[] generarReporteUsuariosExcel() {
        return exportarExcel(prepararReporteUsuarios(), "Usuarios");
    }

    private JasperPrint prepararReporteUsuarios() {
        List<UsuarioReporteItem> items = usuarioService.listarTodos().stream()
                .map(this::mapearUsuario)
                .toList();

        Map<String, Object> params = new HashMap<>();
        params.put("fechaGeneracion", LocalDateTime.now().format(FECHA_HORA_FMT));
        params.put("totalUsuarios", items.size());

        return llenarReporte("reports/reporte_usuarios.jrxml", params, items);
    }

    // --- Mapeadores ---

    private LibroReporteItem mapearLibro(Libro l) {
        String editorial = (l.getEditorial() != null) ? l.getEditorial().getNombre() : "Sin editorial";
        String paginas = (l.getPaginas() != null) ? String.valueOf(l.getPaginas()) : "-";
        String fechaPub = (l.getFechaPublicacion() != null) ? l.getFechaPublicacion().format(FECHA_FMT) : "-";
        String isbn = textoSeguro(l.getIsbn());
        String estado = textoSeguro(l.getEstado());

        return new LibroReporteItem(l.getIdLibro(), l.getTitulo(), isbn, editorial, paginas, fechaPub, estado);
    }

    private UsuarioReporteItem mapearUsuario(Usuario u) {
        String nombreCompleto = (u.getNombres() + " " + u.getApellidos()).trim();
        String rol = (u.getRol() != null) ? u.getRol().getNombre() : "Sin rol";
        String fechaReg = (u.getFechaRegistro() != null) ? u.getFechaRegistro().format(FECHA_FMT) : "-";
        String estado = Boolean.TRUE.equals(u.getActivo()) ? "Activo" : "Inactivo";

        return new UsuarioReporteItem(
                u.getIdUsuario(),
                nombreCompleto,
                textoSeguro(u.getDni()),
                textoSeguro(u.getEmail()),
                rol,
                fechaReg,
                estado
        );
    }

    private String textoSeguro(String valor) {
        return (valor != null && !valor.isBlank()) ? valor : "-";
    }

    // --- Helpers de JasperReports ---

    private JasperPrint llenarReporte(String rutaJrxml, Map<String, Object> params, Collection<?> datos) {
        try (InputStream stream = new ClassPathResource(rutaJrxml).getInputStream()) {
            JasperReport report = JasperCompileManager.compileReport(stream);
            return JasperFillManager.fillReport(report, params, new JRBeanCollectionDataSource(datos));
        } catch (Exception e) {
            throw new RuntimeException("Error al compilar/llenar el reporte: " + e.getMessage(), e);
        }
    }

    private byte[] exportarPdf(JasperPrint print) {
        try {
            return JasperExportManager.exportReportToPdf(print);
        } catch (JRException e) {
            throw new RuntimeException("Error al exportar a PDF: " + e.getMessage(), e);
        }
    }

    private byte[] exportarExcel(JasperPrint print, String nombreHoja) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));

            SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
            config.setSheetNames(new String[]{nombreHoja});
            config.setOnePagePerSheet(false);
            config.setDetectCellType(true);
            config.setRemoveEmptySpaceBetweenRows(true);
            config.setRemoveEmptySpaceBetweenColumns(true);
            config.setWhitePageBackground(false);
            exporter.setConfiguration(config);

            exporter.exportReport();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al exportar a Excel: " + e.getMessage(), e);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class LibroReporteItem {
        private Integer idLibro;
        private String titulo;
        private String isbn;
        private String editorial;
        private String paginas;
        private String fechaPublicacion;
        private String estado;
    }

    @Getter
    @AllArgsConstructor
    public static class UsuarioReporteItem {
        private Integer idUsuario;
        private String nombreCompleto;
        private String dni;
        private String email;
        private String rol;
        private String fechaRegistro;
        private String estado;
    }
}