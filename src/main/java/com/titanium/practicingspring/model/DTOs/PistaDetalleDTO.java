package com.titanium.practicingspring.model.DTOs;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO que devuelve la información de una pista junto con
 * las reservas activas (confirmadas y futuras), para saber
 * exactamente en qué horarios está ocupada.
 */
public class PistaDetalleDTO {

    private int id;
    private String nombre;
    private String estado;
    private Double precioHora;
    private int numeroPistas;

    // Lista de reservas activas (CONFIRMADAS y con hora_fin en el futuro)
    private List<ReservaResumenDTO> reservasActivas;

    // Lista de horas libres en el día de hoy
    private List<String> horasLibresHoy;

    // ---- Clase interna para el resumen de cada reserva ----
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

        public int getIdReserva()          { return idReserva; }
        public String getNombreUsuario()   { return nombreUsuario; }
        public LocalDateTime getHoraInicio() { return horaInicio; }
        public LocalDateTime getHoraFin()    { return horaFin; }
        public String getEstado()          { return estado; }
    }

    // ---- Constructor ----
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

    // ---- Getters ----
    public int getId()                              { return id; }
    public String getNombre()                       { return nombre; }
    public String getEstado()                       { return estado; }
    public Double getPrecioHora()                   { return precioHora; }
    public int getNumeroPistas()                    { return numeroPistas; }
    public List<ReservaResumenDTO> getReservasActivas() { return reservasActivas; }
    public List<String> getHorasLibresHoy()         { return horasLibresHoy; }
}
