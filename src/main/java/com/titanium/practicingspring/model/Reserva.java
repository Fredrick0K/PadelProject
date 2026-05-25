package com.titanium.practicingspring.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_pista")
    private Pista pista;

    @Column(name = "hora_inicio")
    private LocalDateTime horaInicio; 

    @Column(name = "hora_fin")
    private LocalDateTime horaFin;

    private String estado;

    @Column(name = "precio_total")
    private Double precioTotal;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

}
