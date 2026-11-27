package com.bibliot.gestion_biblioteca.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdministracionCatalogosController {
    @GetMapping({"/autores", "/editoriales"})
    public String listado(HttpServletRequest request, Model model) {
        model.addAttribute("tipo", request.getRequestURI().endsWith("/autores") ? "autor" : "editorial");
        return "catalogos";
    }

    @GetMapping({"/autores/{id}", "/editoriales/{id}"})
    public String detalle(@PathVariable Integer id, HttpServletRequest request, Model model) {
        model.addAttribute("tipo", request.getRequestURI().contains("/autores/") ? "autor" : "editorial");
        model.addAttribute("registroId", id);
        return "detalle-catalogo";
    }
}
