package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.service.ReporteService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    private static final MediaType EXCEL_MEDIA_TYPE =
            MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/libros/pdf")
    public ResponseEntity<byte[]> descargarLibrosPdf() {
        return construirDescarga(reporteService.generarReporteLibrosPdf(), MediaType.APPLICATION_PDF, "reporte_libros.pdf");
    }

    @GetMapping("/libros/excel")
    public ResponseEntity<byte[]> descargarLibrosExcel() {
        return construirDescarga(reporteService.generarReporteLibrosExcel(), EXCEL_MEDIA_TYPE, "reporte_libros.xlsx");
    }

    @GetMapping("/usuarios/pdf")
    public ResponseEntity<byte[]> descargarUsuariosPdf() {
        return construirDescarga(reporteService.generarReporteUsuariosPdf(), MediaType.APPLICATION_PDF, "reporte_usuarios.pdf");
    }

    @GetMapping("/usuarios/excel")
    public ResponseEntity<byte[]> descargarUsuariosExcel() {
        return construirDescarga(reporteService.generarReporteUsuariosExcel(), EXCEL_MEDIA_TYPE, "reporte_usuarios.xlsx");
    }

    private ResponseEntity<byte[]> construirDescarga(byte[] archivo, MediaType mediaType, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
        return ResponseEntity.ok().headers(headers).body(archivo);
    }
}