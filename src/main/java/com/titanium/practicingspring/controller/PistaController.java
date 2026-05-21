package com.titanium.practicingspring.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.titanium.practicingspring.model.Pista;
import com.titanium.practicingspring.model.DTOs.DisponibilidadDTO;
import com.titanium.practicingspring.model.DTOs.PistaDetalleDTO;
import com.titanium.practicingspring.service.PistaService;

@RestController
@RequestMapping("/api/pistas")
public class PistaController {

    @Autowired
    private PistaService pistaService;

    // Lista todas las pistas que tenemos
    @GetMapping
    public List<Pista> getAll() {
        return pistaService.findAll();
    }

    // Devuelve los detalles básicos de una pista pasándole su ID
    @GetMapping("/{id}")
    public Pista getById(@PathVariable int id) {
        return pistaService.findById(id);
    }

    // Devuelve el detalle COMPLETO: estado de la pista + reservas activas con horarios
    // Útil para saber exactamente en qué horas está ocupada la pista
    @GetMapping("/{id}/detalle")
    public PistaDetalleDTO getDetalle(@PathVariable int id) {
        return pistaService.getDetalle(id);
    }

    // Devuelve la disponibilidad de una pista para una fecha concreta (YYYY-MM-DD)
    @GetMapping("/{id}/disponibilidad")
    public DisponibilidadDTO getDisponibilidad(
            @PathVariable int id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return pistaService.getDisponibilidad(id, fecha);
    }

    // Un filtro útil: solo devuelve las pistas que están disponibles
    @GetMapping("/activas")
    public List<Pista> getActivas() {
        return pistaService.findActivas();
    }

    // Añade una pista nueva
    @PostMapping
    public Pista save(@RequestBody Pista pista) {
        return pistaService.save(pista);
    }

    // Modifica una pista que ya existía (cambiar precio, estado, etc.)
    @PutMapping("/{id}")
    public Pista update(@PathVariable int id, @RequestBody Pista pista) {
        // Obligamos a que el ID coincida con el que pasaron por la URL
        pista.setId(id);
        return pistaService.save(pista);
    }

    // Borra una pista por completo (cuidado si tiene reservas asociadas)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        pistaService.delete(id);
    }
}
