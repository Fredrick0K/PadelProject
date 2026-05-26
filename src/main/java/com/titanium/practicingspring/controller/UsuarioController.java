package com.titanium.practicingspring.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.titanium.practicingspring.model.Usuario;
import com.titanium.practicingspring.service.UsuarioService;

//RestController indica que esta clase es un controlador MVC de Spring y manejara peticiones HTTP.
//RequestMapping("/api/usuarios") indica que todos los endpoints de esta clase empezaran por /api/usuarios.
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    // Declaramos el servicio de reservas para poder usarlo en los endpoints
    // Autowired crea una instancia de ReservaService en vez de tener que crearla
    // nosotros manualmente con "new ReservaService()".
    @Autowired
    private UsuarioService usuarioService;

    // Devuelve todos los usuarios registrados
    @GetMapping
    public List<Usuario> getAll() {
        return usuarioService.findAll();
    }

    // Busca un usuario concreto por su ID
    @GetMapping("/{id}")
    public Usuario getById(@PathVariable int id) {
        return usuarioService.findById(id);
    }

    // Actualiza los datos de un usuario existente
    @PutMapping("/{id}")
    public Usuario update(@PathVariable int id, @RequestBody Usuario usuario) {
        // Le asignamos el ID de la URL al objeto para asegurarnos de actualizar el
        // correcto
        usuario.setId(id);
        return usuarioService.save(usuario);
    }

    // Borra un usuario usando su ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        usuarioService.delete(id);
    }
}
