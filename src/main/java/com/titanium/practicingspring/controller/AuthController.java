package com.titanium.practicingspring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.titanium.practicingspring.model.Usuario;
import com.titanium.practicingspring.security.JwtUtil;
import com.titanium.practicingspring.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtUtil jwtUtil,
            UsuarioService usuarioService,
            PasswordEncoder passwordEncoder) {

        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------------------------------------------------------------
    // CLASES INTERNAS DE PETICIÓN / RESPUESTA
    // ---------------------------------------------------------------

    public static class AuthRequest {

        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class AuthResponse {

        private String token;

        public AuthResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    // ---------------------------------------------------------------
    // ENDPOINTS
    // ---------------------------------------------------------------

    // Recibe las credenciales y devuelve un token JWT si son correctas
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) throws Exception {

        try {
            // El AuthenticationManager verifica si el email y contraseña coinciden
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.email, authRequest.password));
        } catch (DisabledException e) {
            // Si el usuario está deshabilitado, devolvemos 403 FORBIDDEN
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario deshabilitado", e);
        } catch (Exception e) {
            // Si la contraseña es incorrecta o el usuario no existe (401)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email o contraseña incorrectos", e);
        }

        // Si llegamos aquí, las credenciales son correctas — generamos el token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.email);
        final String jwt = jwtUtil.generateToken(userDetails);

        return new AuthResponse(jwt);
    }

    // Permite a un usuario nuevo registrarse en el sistema
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {

        try {
            // Encriptamos la contraseña antes de guardarla en la BD
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            // Guardamos el usuario — el service asignará fechaCreacion y activo
            // automáticamente
            Usuario nuevoUsuario = usuarioService.save(usuario);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo registrar el usuario", e);
        }
    }
}
