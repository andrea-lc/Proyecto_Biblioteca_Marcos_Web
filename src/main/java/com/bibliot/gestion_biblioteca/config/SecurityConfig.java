
package com.bibliot.gestion_biblioteca.config;

import com.bibliot.gestion_biblioteca.segurity.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // La autenticación se realizará mediante JWT
                .csrf(csrf -> csrf.disable())
                // No se almacenan sesiones en el servidor
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Deshabilitar autenticación HTTP básica y formulario de login
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())
                // Reglas de autorización
                .authorizeHttpRequests(auth -> auth

                        // Login y registro públicos
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/registro").permitAll()

                        // Catálogo: únicamente consultas GET públicas
                        .requestMatchers(HttpMethod.GET, "/api/catalogo/**").permitAll()

                        // Administración: solo administrador
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMINISTRADOR")

                        // Bibliotecario: conserva todos sus permisos
                        .requestMatchers("/api/bibliotecario/**")
                        .hasAnyRole("ADMINISTRADOR", "BIBLIOTECARIO")

                        // Cuenta personal: cualquier usuario autenticado
                        .requestMatchers("/api/lector/me/**")
                        .authenticated()

                        // Rutas de lector
                        .requestMatchers("/api/lector/**")
                        .hasAnyRole("ADMINISTRADOR", "BIBLIOTECARIO", "LECTOR")

                        // Cualquier otra API requiere autenticación
                        .requestMatchers("/api/**").authenticated()

                        // Vistas Thymeleaf y demás recursos
                        .anyRequest().permitAll()
                )
                // Respuesta cuando no existe autenticación válida
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(401, "No autenticado")
                        )
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendError(403, "Acceso denegado")
                        )
                )
                // Ejecutar el filtro JWT antes del filtro de autenticación
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );


        return http.build();
    }
}