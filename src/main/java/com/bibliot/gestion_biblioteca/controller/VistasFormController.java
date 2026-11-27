package com.bibliot.gestion_biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VistasFormController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping(value = "/login", params = "oauth=unavailable")
    public String oauthPendiente(Model model) {
        model.addAttribute("formMessage", "El acceso con Google todavía no está configurado.");
        model.addAttribute("formMessageType", "info");
        return "login";
    }

    @GetMapping(value = "/login", params = "error")
    public String loginError(Model model) {
        model.addAttribute("formMessage", "Correo o contraseña incorrectos.");
        model.addAttribute("formMessageType", "danger");
        return "login";
    }

    @GetMapping(value = "/login", params = "logout")
    public String loginCerrado(Model model) {
        model.addAttribute("formMessage", "Cerraste sesión correctamente.");
        model.addAttribute("formMessageType", "success");
        return "login";
    }

    @GetMapping("/oauth2/authorization/google")
    public String oauthNoConfigurado() {
        return "redirect:/login?oauth=unavailable";
    }
}
