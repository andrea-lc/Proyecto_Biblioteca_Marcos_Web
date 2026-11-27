package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.service.ReporteDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bibliotecario/reportes")
@RequiredArgsConstructor
public class ReporteDashboardController {
    private final ReporteDashboardService service;

    @GetMapping("/dashboard")
    public ReporteDashboardService.Resumen dashboard() { return service.obtenerResumen(); }
}
