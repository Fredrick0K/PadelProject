package com.titanium.practicingspring.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.titanium.practicingspring.model.Pista;
import com.titanium.practicingspring.model.Reserva;
import com.titanium.practicingspring.model.DTOs.DisponibilidadDTO;
import com.titanium.practicingspring.model.DTOs.PistaDetalleDTO;
import com.titanium.practicingspring.repository.IRepository.IPistaRepository;
import com.titanium.practicingspring.repository.IRepository.IReservaRepository;

// Esta clase representa la logica de negocio de la entidad Pista. 
// Aqui es donde se realizan todas las operaciones que se usan en el Controller.
@Service
public class PistaService {

    @Autowired
    private IPistaRepository pistaRepo;

    // Declaramos el repositorio de reserva ya que necesitamos saber las horas
    // ocupadas
    @Autowired
    private IReservaRepository reservaRepo;

    // Busca todas las pistas
    public List<Pista> findAll() {
        return pistaRepo.findAll();
    }

    // Busca por ID
    public Pista findById(int id) {
        // Si no existe, lanzamos 404 en vez de devolver null
        return pistaRepo.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pista no encontrada con id: " + id));
    }

    // filtro para buscar las pistas Disponibles solo.
    public List<Pista> findActivas() {
        return pistaRepo.findByEstado("DISPONIBLE");
    }

    // Guarda un objeto en la base de datos. El metodo save ya esta implementado por
    // el repositorio padre JpaRepository entonces solo lo llamamos y evitamos
    // escribir consultas SQL a mano.
    public Pista save(Pista pista) {
        if (pista.getId() == null) {
            pista.setFechaCreacion(LocalDateTime.now());
            if (pista.getEstado() == null) {
                pista.setEstado("DISPONIBLE");
            }
        }
        return pistaRepo.save(pista);
    }

    // Borra una pista por ID
    public void delete(int id) {
        pistaRepo.deleteById(id);
    }

    /**
     * Devuelve el detalle completo de una pista: su estado actual
     * más la lista de reservas CONFIRMADAS con hora_fin en el futuro,
     * para saber exactamente en qué horarios está ocupada.
     */
    public PistaDetalleDTO getDetalle(int pistaId) {
        Pista pista = findById(pistaId);
        java.time.LocalDate hoy = java.time.LocalDate.now();

        // Cogemos las reservas confirmadas de la pista DESDE HOY EN ADELANTE
        List<Reserva> reservas = reservaRepo.findByPistaId(pistaId);
        List<PistaDetalleDTO.ReservaResumenDTO> activas = reservas.stream()
                .filter(r -> "CONFIRMADA".equals(r.getEstado()))
                .filter(r -> r.getHoraInicio() != null && !r.getHoraInicio().toLocalDate().isBefore(hoy))
                .map(r -> new PistaDetalleDTO.ReservaResumenDTO(
                        r.getId(),
                        r.getUsuario() != null ? r.getUsuario().getNombre() : "Desconocido",
                        r.getHoraInicio(),
                        r.getHoraFin(),
                        r.getEstado()))
                .collect(Collectors.toList());

        // Calcular horas libres para hoy (ejemplo: 08:00 a 23:00)
        List<String> horasLibresHoy = new java.util.ArrayList<>();
        for (int i = 8; i < 23; i++) {
            LocalDateTime inicioSlot = hoy.atTime(i, 0);
            LocalDateTime finSlot = hoy.atTime(i + 1, 0);

            boolean ocupado = activas.stream()
                    .anyMatch(r -> (inicioSlot.isBefore(r.getHoraFin()) && finSlot.isAfter(r.getHoraInicio())));

            if (!ocupado) {
                horasLibresHoy.add(String.format("%02d:00 - %02d:00", i, i + 1));
            }
        }

        return new PistaDetalleDTO(
                pista.getId(),
                pista.getNombre(),
                pista.getEstado(),
                pista.getPrecioHora(),
                pista.getNumeroPista(),
                activas,
                horasLibresHoy);
    }

    public DisponibilidadDTO getDisponibilidad(int pistaId, LocalDate fecha) {
        Pista pista = findById(pistaId);

        List<Reserva> reservas = reservaRepo.findByPistaId(pistaId);
        List<DisponibilidadDTO.ReservaResumenDTO> ocupadas = reservas.stream()
                .filter(r -> "CONFIRMADA".equals(r.getEstado()))
                .filter(r -> r.getHoraInicio() != null && r.getHoraInicio().toLocalDate().equals(fecha))
                .map(r -> new DisponibilidadDTO.ReservaResumenDTO(
                        r.getId(),
                        r.getUsuario() != null ? r.getUsuario().getNombre() : "Desconocido",
                        r.getHoraInicio(),
                        r.getHoraFin(),
                        r.getEstado()))
                .collect(Collectors.toList());

        List<String> horasLibres = new java.util.ArrayList<>();
        for (int i = 8; i < 23; i++) {
            LocalDateTime inicioSlot = fecha.atTime(i, 0);
            LocalDateTime finSlot = fecha.atTime(i + 1, 0);

            boolean ocupado = ocupadas.stream()
                    .anyMatch(r -> (inicioSlot.isBefore(r.getHoraFin()) && finSlot.isAfter(r.getHoraInicio())));

            if (!ocupado) {
                horasLibres.add(String.format("%02d:00 - %02d:00", i, i + 1));
            }
        }

        return new DisponibilidadDTO(
                pista.getId(),
                pista.getNombre(),
                fecha,
                ocupadas,
                horasLibres);
    }
}
