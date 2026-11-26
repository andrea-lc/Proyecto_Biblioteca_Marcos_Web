package com.bibliot.gestion_biblioteca.segurity;

import java.util.Date;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final String SECRET = "12345678912345678912345678912345"; // mínimo 32 caracteres

    public String generateToken(String username, String rol, Integer uid, String nombre) {
        return Jwts.builder()
                .subject(username)
                .claim("rol", rol)
                .claim("uid", uid)
                .claim("nombre", nombre)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), Jwts.SIG.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}