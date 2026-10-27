package com.bibliot.gestion_biblioteca.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/").setViewName("index");

        registry.addViewController("/index").setViewName("index");

        registry.addViewController("/login").setViewName("login");

        registry.addViewController("/registro").setViewName("registro");

        registry.addViewController("/explorar").setViewName("explorar");

        registry.addViewController("/ayuda").setViewName("ayuda");

        registry.addViewController("/perfil").setViewName("perfil-lector");

        registry.addViewController("/mis-prestamos").setViewName("mis-prestamos");

        registry.addViewController("/notificaciones").setViewName("notificaciones");

        registry.addViewController("/terminos").setViewName("terminos");

        registry.addViewController("/lector-digital").setViewName("lector-digital");

        registry.addViewController("/detalle-fisico").setViewName("detalle-fisico");

        registry.addViewController("/detalle_fisico").setViewName("detalle-fisico");

        registry.addViewController("/detalle-virtual").setViewName("detalle-virtual");

        registry.addViewController("/detalle_virtual").setViewName("detalle-virtual");

        registry.addViewController("/dashboard").setViewName("dashboard");

        registry.addViewController("/usuarios").setViewName("usuarios");

        registry.addViewController("/usuarios/detalle").setViewName("detalle-usuario");

        registry.addViewController("/usuarios/nuevo").setViewName("nuevo-usuario");
        
        registry.addViewController("/usuarios/editar").setViewName("editar-usuario");

        registry.addViewController("/libros").setViewName("libros");

        registry.addViewController("/libros/nuevo").setViewName("nuevo-libro");

        registry.addViewController("/libros/editar").setViewName("editar-libro");

        registry.addViewController("/configuracion").setViewName("configuracion");
        
        registry.addViewController("/roles").setViewName("roles");
    }

}
