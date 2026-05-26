package com.titanium.practicingspring.repository.IRepository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.titanium.practicingspring.model.Pista;

// Repositorio para la entidad Pista, con metodos personalizados para buscar por estado.
// Los repositorios de JpaRepository ya tienen metodos basicos como findAll(), findById(), save(), delete() etc,
// pero podemos añadir los que queramos para extender su funcionalidad.
@Repository
public interface IPistaRepository extends JpaRepository<Pista, Integer> {

    List<Pista> findByEstado(String estado);

}
