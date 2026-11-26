package com.bibliot.gestion_biblioteca.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bibliot.gestion_biblioteca.entities.Rol;
import com.bibliot.gestion_biblioteca.entities.Usuario;
import com.bibliot.gestion_biblioteca.service.RolService;
import com.bibliot.gestion_biblioteca.service.UsuarioService;

@Controller
public class VistasFormController {
    private final UsuarioService usuarios;
    private final RolService roles;

    public VistasFormController(UsuarioService usuarios, RolService roles) {
        this.usuarios = usuarios;
        this.roles = roles;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/registro")
    public String registroPage() {
        return "registro";
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

    @PostMapping("/registro")
    public String registrar(@RequestParam(required = false) String nombres,
            @RequestParam(required = false) String dni,
            @RequestParam(name = "correo", required = false) String email,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) String password,
            @RequestParam(name = "confirmarPassword", required = false) String confirmarPassword,
            Model model, RedirectAttributes redirectAttributes) {

        String error = validar(nombres, dni, email, telefono, direccion, password, confirmarPassword);
        if (error != null) {
            model.addAttribute("formMessage", error);
            model.addAttribute("formMessageType", "danger");
            return "registro";
        }

        try {
            String[] partesNombre = nombres.trim().split("\\s+", 2);
            Usuario usuario = new Usuario();
            usuario.setNombres(partesNombre[0]);
            usuario.setApellidos(partesNombre.length > 1 ? partesNombre[1] : partesNombre[0]);
            usuario.setDni(dni.trim());
            usuario.setEmail(email.trim());
            usuario.setTelefono(telefono.trim());
            usuario.setDireccion(direccion == null || direccion.isBlank() ? null : direccion.trim());
            usuario.setPassword(password);
            usuario.setRol(obtenerRolLector());
            usuario.setFechaRegistro(LocalDateTime.now());
            usuario.setActivo(true);

            usuarios.crearUsuario(usuario);
            redirectAttributes.addFlashAttribute("formMessage", "Cuenta creada correctamente. Ya puedes iniciar sesión.");
            redirectAttributes.addFlashAttribute("formMessageType", "success");
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("formMessage", "No se pudo completar el registro: " + e.getMessage());
            model.addAttribute("formMessageType", "danger");
        }
        return "registro";
    }

    private String validar(String nombres, String dni, String email, String telefono, String direccion,
            String password, String confirmarPassword) {
        if (nombres == null || nombres.isBlank()) {
            return "Ingresa tus nombres y apellidos.";
        }
        if (nombres.length() > 100) {
            return "Los nombres no deben superar 100 caracteres.";
        }
        if (dni == null || !dni.matches("\\d{8}")) {
            return "El DNI debe tener exactamente 8 dígitos.";
        }
        if (email == null || email.length() > 150 || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            return "Ingresa un correo electrónico válido (máximo 150 caracteres).";
        }
        if (telefono == null || !telefono.matches("\\d{9}")) {
            return "El teléfono debe tener exactamente 9 dígitos.";
        }
        if (direccion != null && direccion.length() > 200) {
            return "La dirección no debe superar 200 caracteres.";
        }
        if (password == null || password.length() < 8 || password.length() > 255) {
            return "La contraseña debe tener entre 8 y 255 caracteres.";
        }
        if (!password.equals(confirmarPassword)) {
            return "Las contraseñas no coinciden.";
        }
        return null;
    }

    private Rol obtenerRolLector() {
        try {
            return roles.buscarPorNombre("Lector");
        } catch (RuntimeException sinRol) {
            Rol lector = new Rol();
            lector.setNombre("Lector");
            return roles.guardar(lector);
        }
    }

    @GetMapping("/oauth2/authorization/google")
    public String oauthNoConfigurado() {
        return "redirect:/login?oauth=unavailable";
    }
}
