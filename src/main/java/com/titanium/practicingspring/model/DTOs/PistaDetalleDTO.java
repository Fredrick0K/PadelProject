package com.titanium.practicingspring.model.DTOs;

import java.time.LocalDateTime;
import java.util.List;

// Se usan DTOs para definir la estructura de los datos que se envían o reciben en los endpoints.
// No tienen lógica de negocio, solo getters y setters. 
// Es una buena práctica usar DTOs para no exponer las entidades de la base de datos.
public class PistaDetalleDTO {

    private int id;
    private String nombre;
    private String estado;
    private Double precioHora;
    private int numeroPistas;

    private List<ReservaResumenDTO> reservasActivas;

    private List<String> horasLibresHoy;

    // Clase interna para el resumen de cada reserva
    public static class ReservaResumenDTO {
        private int idReserva;
        private String nombreUsuario;
        private LocalDateTime horaInicio;
        private LocalDateTime horaFin;
        private String estado;

        public ReservaResumenDTO(int idReserva, String nombreUsuario,
                LocalDateTime horaInicio, LocalDateTime horaFin,
                String estado) {
            this.idReserva = idReserva;
            this.nombreUsuario = nombreUsuario;
            this.horaInicio = horaInicio;
            this.horaFin = horaFin;
            this.estado = estado;
        }

        public int getIdReserva() {
            return idReserva;
        }

        public String getNombreUsuario() {
            return nombreUsuario;
        }

        public LocalDateTime getHoraInicio() {
            return horaInicio;
        }

        public LocalDateTime getHoraFin() {
            return horaFin;
        }

        public String getEstado() {
            return estado;
        }
    }

    public PistaDetalleDTO(int id, String nombre, String estado,
            Double precioHora, int numeroPistas,
            List<ReservaResumenDTO> reservasActivas,
            List<String> horasLibresHoy) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.precioHora = precioHora;
        this.numeroPistas = numeroPistas;
        this.reservasActivas = reservasActivas;
        this.horasLibresHoy = horasLibresHoy;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEstado() {
        return estado;
    }

    public Double getPrecioHora() {
        return precioHora;
    }

    public int getNumeroPistas() {
        return numeroPistas;
    }

    public List<ReservaResumenDTO> getReservasActivas() {
        return reservasActivas;
    }

    public List<String> getHorasLibresHoy() {
        return horasLibresHoy;
    }
}
