package com.titanium.practicingspring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.titanium.practicingspring.model.Reserva;
import com.titanium.practicingspring.service.ReservaService;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    // Muestra todas las reservas del sistema
    @GetMapping
    public List<Reserva> getAll() {
        return reservaService.findAll();
    }

    // Busca los detalles de una reserva en concreto
    @GetMapping("/{id}")
    public Reserva getById(@PathVariable int id) {
        return reservaService.findById(id);
    }

    // Encuentra todas las reservas que ha hecho un usuario específico
    @GetMapping("/usuario/{usuarioId}")
    public List<Reserva> getByUsuario(@PathVariable int usuarioId) {
        return reservaService.findByUsuarioId(usuarioId);
    }

    // Encuentra todas las reservas de una pista específica
    @GetMapping("/pista/{pistaId}")
    public List<Reserva> getByPista(@PathVariable int pistaId) {
        return reservaService.findByPistaId(pistaId);
    }

    // Crea una nueva reserva
    @PostMapping
    public Reserva save(@RequestBody Reserva reserva) {
        return reservaService.save(reserva);
    }

    // Actualiza una reserva (por si cambian la hora o se cancela)
    @PutMapping("/{id}")
    public Reserva update(@PathVariable int id, @RequestBody Reserva reserva) {
        // Fijamos el ID para no crear una reserva nueva por accidente
        reserva.setId(id);
        return reservaService.save(reserva);
    }

    // Borra la reserva
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        reservaService.delete(id);
    }
}
