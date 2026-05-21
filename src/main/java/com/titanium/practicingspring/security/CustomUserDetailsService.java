package com.titanium.practicingspring.security;

import com.titanium.practicingspring.model.Usuario;
import com.titanium.practicingspring.repository.IRepository.IUsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final IUsuarioRepository usuarioRepository;

    public CustomUserDetailsService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Este método es usado por Spring Security internamente cuando intenta autenticar a alguien
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Buscamos a nuestro usuario en la base de datos usando su email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
        
        // Convertimos nuestro "Usuario" (entidad) al "UserDetails" que entiende Spring Security
        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getContrasena())
                // Si no tiene rol en la BBDD, le damos el rol "USER" por defecto
                .roles(usuario.getRol() != null ? usuario.getRol() : "USER")
                // Si el campo "activo" es false, marcamos la cuenta como deshabilitada (Spring rechazará el login)
                .disabled(!usuario.isActivo())
                .build();
    }
}
