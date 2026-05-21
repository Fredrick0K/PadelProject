package com.titanium.practicingspring.repository.IRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.titanium.practicingspring.model.Usuario;
import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer>{
    Optional<Usuario> findByEmail(String email);
}
