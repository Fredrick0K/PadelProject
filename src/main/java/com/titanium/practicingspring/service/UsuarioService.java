package com.titanium.practicingspring.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.titanium.practicingspring.model.Usuario;
import com.titanium.practicingspring.repository.IRepository.IUsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> findAll() {
        return usuarioRepo.findAll();
    }

    public Usuario findById(int id) {
        // Si no existe, lanzamos 404 en vez de devolver null
        return usuarioRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con id: " + id));
    }

    public Usuario save(Usuario usuario) {
        // Si la contraseña viene en texto plano, la encriptamos antes de guardar
        if (usuario.getContrasena() != null && !usuario.getContrasena().isEmpty()) {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }

        if (usuario.getId() == 0 && usuario.getFechaCreacion() == null) {
            usuario.setFechaCreacion(LocalDateTime.now());
            usuario.setActivo(true);
        }
        return usuarioRepo.save(usuario);
    }

    public void delete(int id) {
        usuarioRepo.deleteById(id);
    }
}
