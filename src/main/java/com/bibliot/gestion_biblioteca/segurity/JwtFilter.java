package com.bibliot.gestion_biblioteca.segurity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            try {
                String token = header.substring(7).trim();

                Claims claims = jwtService.extractClaims(token);

                String username = claims.getSubject();
                String rol = claims.get("rol", String.class);

                if (username == null || username.isBlank()
                        || rol == null || rol.isBlank()) {
                    SecurityContextHolder.clearContext();
                    response.sendError(
                            HttpServletResponse.SC_UNAUTHORIZED,
                            "Token sin usuario o rol válido"
                    );
                    return;
                }

                // Administrador -> ADMINISTRADOR
                // Bibliotecario -> BIBLIOTECARIO
                // Lector -> LECTOR
                rol = rol.trim()
                        .toUpperCase(Locale.ROOT)
                        .replace(" ", "_");

                // Evita duplicar ROLE_ si el token ya lo contiene
                if (rol.startsWith("ROLE_")) {
                    rol = rol.substring(5);
                }

                List<String> rolesPermitidos = List.of(
                        "ADMINISTRADOR",
                        "BIBLIOTECARIO",
                        "LECTOR"
                );

                if (!rolesPermitidos.contains(rol)) {
                    SecurityContextHolder.clearContext();
                    response.sendError(
                            HttpServletResponse.SC_FORBIDDEN,
                            "Rol no reconocido"
                    );
                    return;
                }

                var authority = new SimpleGrantedAuthority("ROLE_" + rol);

                var authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(authority)
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

            } catch (JwtException | IllegalArgumentException e) {
                SecurityContextHolder.clearContext();
                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Token inválido o expirado"
                );
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
