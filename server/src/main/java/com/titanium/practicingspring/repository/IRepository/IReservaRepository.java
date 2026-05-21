package com.titanium.practicingspring.repository.IRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.titanium.practicingspring.model.Reserva;

public interface IReservaRepository extends JpaRepository<Reserva, Integer>{

    List<Reserva> findByUsuarioId( int usuarioId);
    List<Reserva> findByPistaId( int pistaId);


    @Query("SELECT r FROM Reserva r WHERE r.pista.id = :pistaId " +
    "AND r.estado != :estado " +
    "AND (r.horaInicio < :horaFin " +
   " AND r.horaFin > :horaInicio)")

   List<Reserva> findReservaSolapada(
    @Param("pistaId") int pistaId,
    @Param("estado") String estado,
    @Param("horaInicio") LocalDateTime horaInicio,
    @Param("horaFin") LocalDateTime horaFin
   );
}
