package com.titanium.practicingspring.repository.IRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.titanium.practicingspring.model.Usuario;
import java.util.Optional;

// Repositorio para la entidad Usuario, con metodos personalizados para buscar por email.
// Los repositorios de JpaRepository ya tienen metodos basicos como findAll(), findById(), save(), delete() etc,
// pero podemos añadir los que queramos para extender su funcionalidad.
public interface IUsuarioRepository extends JpaRepository<Usuario, Integer>{
    Optional<Usuario> findByEmail(String email);
}
