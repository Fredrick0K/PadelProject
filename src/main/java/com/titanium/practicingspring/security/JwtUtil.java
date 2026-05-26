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


// Esta clase crea el token apartir de los datos del usuario y extrae informacion para verificarlo.
@Component
public class JwtUtil {

    // Clave secreta leída desde application-postgres.properties. Para evitar hardcodearla en el codigo. 
    @Value("${jwt.secret}")
    private String secretKeyString;

    // Convierte la cadena del secret en una SecretKey para firmar los tokens.
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

    // Extrae la fecha de expiracion del token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extrae cualquier dato del token que se le pide atraves de los parametros del metodo.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extrae todos los datos del token y si no son validos, tira excepcion y JWTRequestFilter no autenticara a nadie.
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secret())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // verifica si el token ya ha expirado comparando la fecha de caducidad del token con la fecha actual.
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ---------------------------------------------------------------
    // GENERACIÓN DEL TOKEN
    // ---------------------------------------------------------------

    // Se generar un token a partir de los datos de un usuario
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    // Crea el token con los datos del usuario, las fechas e creacion y expiracion y lo firma.
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims) // Datos del usuario
                .subject(subject) // Email del usuario
                .issuedAt(new Date(System.currentTimeMillis())) // Fecha de creación
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 5)) // Expira en 5 horas
                .signWith(secret()) // Firmado con la clave secreta
                .compact(); //Devuelve el token como cadena
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
