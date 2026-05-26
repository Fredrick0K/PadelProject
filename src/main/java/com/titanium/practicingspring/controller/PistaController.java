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

//RestController indica que esta clase es un controlador MVC de Spring y manejara peticiones HTTP.
//RequestMapping("/api/pistas") indica que todos los endpoints de esta clase empezaran por /api/pistas.
@RestController
@RequestMapping("/api/pistas")
public class PistaController {

    // Declaramos el servicio de pistas para poder usarlo en los endpoints
    // Autowired crea una instancia de PistaService en vez de tener que crearla
    // nosotros manualmente con "new PistaService()".
    @Autowired
    private PistaService pistaService;

    // Lista todas las pistas que tenemos
    @GetMapping
    public List<Pista> getAll() {
        return pistaService.findAll();
    }

    // Devuelve los detalles básicos de una pista pasandole su ID
    @GetMapping("/{id}")
    public Pista getById(@PathVariable int id) {
        return pistaService.findById(id);
    }

    // Devuelve el detalle de una pista por ID: estado de la pista + reservas
    // activas con horarios
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

    // Filtr que solo devuelve las pistas que están disponibles
    @GetMapping("/activas")
    public List<Pista> getActivas() {
        return pistaService.findActivas();
    }

    // Añade una pista nueva
    @PostMapping
    public Pista save(@RequestBody Pista pista) {
        return pistaService.save(pista);
    }

    // Modifica una pista por su ID
    @PutMapping("/{id}")
    public Pista update(@PathVariable int id, @RequestBody Pista pista) {
        // Asignamos el ID de la URL al objeto para asegurarnos de actualizar el
        // correcto
        pista.setId(id);
        return pistaService.save(pista);
    }

    // Borra una pista por completo. las resevas se borraran ya que tiene Delete On
    // cascade.
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        pistaService.delete(id);
    }
}
