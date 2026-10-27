package com.bibliot.gestion_biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class VistasFormController {
    @GetMapping(value = "/login", params = "oauth=unavailable")
    public String oauthPendiente(Model model) {
        model.addAttribute("formMessage", "El acceso con Google todavía no está configurado.");
        return "login";
    }

    @PostMapping("/login")
    public String loginPendiente(Model model) {
        model.addAttribute("formMessage", "La autenticación todavía no está conectada.");
        return "login";
    }

    @PostMapping("/registro")
    public String registroPendiente(Model model) {
        model.addAttribute("formMessage", "El registro todavía no está conectado y no se guardaron los datos.");
        return "registro";
    }

    @GetMapping("/oauth2/authorization/google")
    public String oauthNoConfigurado() {
        return "redirect:/login?oauth=unavailable";
    }
}
