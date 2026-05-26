package com.titanium.practicingspring.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Esta clase define la configuracion de seguridad de la API.
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    // Configuramos las reglas de seguridad de la API. Permitimos acceso sin
    // autenticacion a los enpoints de auth y error.
    // El swagger es para pruebas.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desactivamos CSRF porque vamos a usar Tokens (JWT) y no necesitamos
                // protección de formularios web
                // Los :: significan que le estamos pasando el método "disable" como referencia.
                // Sin tener que instanciarlo manualmene.
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        // Permitimos a cualquiera intentar iniciar sesión o ver la documentación de
                        // Swagger
                        .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/error")
                        .permitAll()
                        // Para cualquier otra ruta, pedimos autenticacion
                        .anyRequest().authenticated())
                // Indicamos que la sesion es sin estado (stateless) porque vamos a usar JWT y
                // no queremos que Spring cree sesiones para cada usuario, ya que usaremos en su
                // lugar tokens JWT.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Añadimos nuestro filtro personalizado antes del filtro de usuario/contraseña
                // habitual
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        // Construimos la configuracion http para que spring lo entienda.
        return http.build();
    }

    // Le dice a Spring Security cómo tiene que autenticar a los usuarios
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Usamos BCrypt para encriptar las contraseñas ya que es el recomendado por
    // Spring.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
