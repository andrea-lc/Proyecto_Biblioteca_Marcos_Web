package com.bibliot.gestion_biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller 
public class pagina {

    @GetMapping("/dashboard")
    public String dashboard(Model model){
        model.addAttribute("paginaActual","Dashboard");
        return "dashboard";
    }
    
    
}
