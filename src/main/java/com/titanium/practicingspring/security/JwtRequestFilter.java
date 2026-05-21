package com.titanium.practicingspring.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        // 1. Extraemos el header "Authorization" de la petición HTTP
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 2. Comprobamos si el header existe y si empieza por "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Quitamos "Bearer " para quedarnos solo con el token
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                // Si el token es inválido o ha caducado, ignoramos y dejamos pasar sin autenticar
            }
        }

        // 3. Si hay un usuario en el token, pero aún no está autenticado en Spring Security...
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Buscamos al usuario en la base de datos
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 4. Si el token es válido (no está caducado y pertenece a este usuario)
            if (jwtUtil.validateToken(jwt, userDetails)) {

                // Le decimos a Spring Security "este usuario es de confianza, déjale pasar"
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 5. Dejamos que la petición continúe su curso hacia el Controller
        chain.doFilter(request, response);
    }
}
