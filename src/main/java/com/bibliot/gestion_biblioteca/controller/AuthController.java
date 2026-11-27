package com.bibliot.gestion_biblioteca.controller;

import com.bibliot.gestion_biblioteca.dto.LoginDtos;
import com.bibliot.gestion_biblioteca.dto.RegistroDtos;
import com.bibliot.gestion_biblioteca.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // LOGIN: login.html hace fetch POST con JSON y guarda el token que devuelve
    @PostMapping("/auth/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody LoginDtos request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            return ResponseEntity.status(401).build();
        }
        String token = authService.login(request.getEmail().trim(), request.getPassword());
        return token != null ? ResponseEntity.ok(token) : ResponseEntity.status(401).build();
    }

    // Vista de registro público (exclusivo para lectores)
    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    // REGISTRO: registro.html envía el formulario a /registro (solo asigna rol Lector)
    @PostMapping("/registro")
    public String registro(@ModelAttribute RegistroDtos form, Model model) {
        try {
            authService.registrar(form);
        } catch (IllegalArgumentException e) {
            model.addAttribute("formMessage", e.getMessage());
            model.addAttribute("formMessageType", "danger");
            return "registro";
        } catch (RuntimeException e) {
            String msg = e.getMessage() != null && e.getMessage().contains("ya está registrado")
                    ? e.getMessage()
                    : "No se pudo crear la cuenta. Inténtalo de nuevo.";
            model.addAttribute("formMessage", msg);
            model.addAttribute("formMessageType", "danger");
            return "registro";
        }
        return "redirect:/login?registrado"; // login.html ya muestra "Cuenta creada correctamente"
    }
}