package com.bibliot.gestion_biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller 
public class pagina {


    @GetMapping("/login")
    public String login(@RequestParam(name = "oauth", required = false) String oauth, Model model) {
        if ("unavailable".equals(oauth)) {
            model.addAttribute("formMessage", "El acceso con Google todavía no está configurado.");
        }
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(Model model) {
        model.addAttribute("formMessage", "La autenticación todavía no está conectada.");
        return "login";
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(Model model) {
        model.addAttribute("formMessage", "El registro todavía no está conectado y no se guardaron los datos.");
        return "registro";
    }

    @GetMapping("/oauth2/authorization/google")
    public String googleNoConfigurado() {
        return "redirect:/login?oauth=unavailable";
    }

    @GetMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    @GetMapping("/explorar")
    public String explorar() {
        return "explorar";
    }

    @GetMapping({"/detalle_fisico", "/detalle-fisico"})
    public String detalleFisico() {
        return "detalle-fisico";
    }

    @GetMapping({"/detalle_virtual", "/detalle-virtual"})
    public String detalleVirtual() {
        return "detalle-virtual";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/usuarios")
    public String usuarios() {
        return "usuarios";
    }

    @GetMapping("/usuarios/detalle")
    public String detalleUsuario() {
        return "detalle-usuario";
    }

    @GetMapping("/libros")
    public String libros() {
        return "libros";
    }

    @GetMapping("/libros/nuevo")
    public String nuevoLibro() {
        return "nuevo-libro";
    }

    @PostMapping("/libros/nuevo")
    public String crearLibro(Model model) {
        model.addAttribute("formMessage", "El formulario se recibió, pero el guardado de libros todavía no está conectado.");
        return "nuevo-libro";
    }
    
    
}
