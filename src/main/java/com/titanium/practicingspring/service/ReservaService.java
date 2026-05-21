package com.titanium.practicingspring.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.titanium.practicingspring.model.Pista;
import com.titanium.practicingspring.model.Reserva;
import com.titanium.practicingspring.repository.IRepository.IReservaRepository;

@Service
public class ReservaService {

    @Autowired
    private IReservaRepository reservaRepo;

    // Inyectamos PistaService para poder actualizar el estado de la pista
    @Autowired
    private PistaService pistaService;

    public List<Reserva> findAll() {
        return reservaRepo.findAll();
    }

    public Reserva findById(int id) {
        // Si no existe, lanzamos 404 en vez de devolver null
        return reservaRepo.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada con id: " + id));
    }

    public List<Reserva> findByUsuarioId(int usuarioId) {
        return reservaRepo.findByUsuarioId(usuarioId);
    }

    public List<Reserva> findByPistaId(int pistaId) {
        return reservaRepo.findByPistaId(pistaId);
    }

    public Reserva save(Reserva reserva) {
        // Validamos que las horas sean obligatorias
        if (reserva.getHoraInicio() == null || reserva.getHoraFin() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La hora de inicio y fin son obligatorias");
        }
        if (reserva.getId() == 0 && reserva.getFechaCreacion() == null) {
            reserva.setFechaCreacion(LocalDateTime.now());
            if (reserva.getEstado() == null) {
                reserva.setEstado("CONFIRMADA");
            }
        }

        // Comprobamos que no haya solapamiento con otra reserva CONFIRMADA en la misma
        // pista
        if (reserva.getPista() != null) {
            List<Reserva> solapadas = reservaRepo.findReservaSolapada(
                    reserva.getPista().getId(),
                    "CANCELADA",
                    reserva.getHoraInicio(),
                    reserva.getHoraFin());

            // Usamos stream para no mutar la lista (JPA devuelve listas inmutables)
            // En actualizaciones (PUT) excluimos la propia reserva de la comprobación
            boolean haySolapamiento = solapadas.stream()
                    .anyMatch(r -> r.getId() != reserva.getId());

            if (haySolapamiento) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "La pista ya está reservada en ese horario");
            }
        }

        // Si la reserva está confirmada, marcamos la pista como "RESERVADA"
        if ("CONFIRMADA".equals(reserva.getEstado()) && reserva.getPista() != null) {
            Pista pista = pistaService.findById(reserva.getPista().getId());
            pista.setEstado("RESERVADA");
            pistaService.save(pista);
        }

        // Si la reserva se cancela, liberamos la pista de nuevo
        if ("CANCELADA".equals(reserva.getEstado()) && reserva.getPista() != null) {
            Pista pista = pistaService.findById(reserva.getPista().getId());
            pista.setEstado("DISPONIBLE");
            pistaService.save(pista);
        }

        return reservaRepo.save(reserva);
    }

    public void delete(int id) {
        // Antes de borrar, recuperamos la reserva para liberar la pista
        Reserva reserva = findById(id);
        if (reserva.getPista() != null) {
            Pista pista = pistaService.findById(reserva.getPista().getId());
            pista.setEstado("DISPONIBLE");
            pistaService.save(pista);
        }
        reservaRepo.deleteById(id);
    }
}
