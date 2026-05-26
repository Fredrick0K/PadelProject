package com.titanium.practicingspring.repository.IRepository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.titanium.practicingspring.model.Reserva;

// Repositorio para la entidad Reserva, con metodos personalizados para buscar por usuario, pista y reservas solapadas.
// Los repositorios de JpaRepository ya tienen metodos basicos como findAll(), findById(), save(), delete() etc,
// pero podemos añadir los que queramos para extender su funcionalidad.
public interface IReservaRepository extends JpaRepository<Reserva, Integer> {

    List<Reserva> findByUsuarioId(int usuarioId);

    List<Reserva> findByPistaId(int pistaId);

    // Consulta personalizada para encontrar reservas que se solapan con un horario dado en una pista concreta
    @Query("SELECT r FROM Reserva r WHERE r.pista.id = :pistaId " +
            "AND r.estado != :estado " +
            "AND (r.horaInicio < :horaFin " +
            " AND r.horaFin > :horaInicio)")

    // Aqui se guardan las reservas que se solapan con el horario que se quiere reservar, para comprobar si se puede reservar o no.
    List<Reserva> findReservaSolapada(
            @Param("pistaId") int pistaId,
            @Param("estado") String estado,
            @Param("horaInicio") LocalDateTime horaInicio,
            @Param("horaFin") LocalDateTime horaFin);
}
