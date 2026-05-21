package com.titanium.practicingspring.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Clave secreta leída desde application.properties — así no queda expuesta en el código fuente
    @Value("${jwt.secret}")
    private String secretKeyString;

    // Construimos la SecretKey en cada uso, para asegurar que @Value ya está inyectado
    private SecretKey secret() {
        return Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }


    // ---------------------------------------------------------------
    // EXTRACCIÓN DE DATOS DEL TOKEN
    // ---------------------------------------------------------------

    // Extrae el nombre de usuario (email) guardado dentro del token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secret())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    // ---------------------------------------------------------------
    // GENERACIÓN DEL TOKEN
    // ---------------------------------------------------------------

    // Método principal para generar un token a partir de los datos de un usuario
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)                                                          // Email del usuario
                .issuedAt(new Date(System.currentTimeMillis()))                            // Fecha de creación
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 10))  // Expira en 10 horas
                .signWith(secret())                                                        // Firmado con la clave secreta
                .compact();
    }


    // ---------------------------------------------------------------
    // VALIDACIÓN DEL TOKEN
    // ---------------------------------------------------------------

    // Verifica que el token pertenece al usuario y que no ha caducado
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}
