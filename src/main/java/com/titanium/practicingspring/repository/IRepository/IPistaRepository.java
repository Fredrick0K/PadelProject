package com.titanium.practicingspring.repository.IRepository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.titanium.practicingspring.model.Pista;

@Repository
public interface IPistaRepository extends JpaRepository<Pista, Integer>{

    List<Pista> findByEstado(String estado);

}
