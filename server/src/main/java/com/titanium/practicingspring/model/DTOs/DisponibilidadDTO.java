package com.titanium.practicingspring.model.DTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DisponibilidadDTO {

    private int idPista;
    private String nombrePista;
    private LocalDate fecha;
    private List<ReservaResumenDTO> ocupadas;
    private List<String> horasLibres;

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

    public DisponibilidadDTO(int idPista, String nombrePista, LocalDate fecha,
                              List<ReservaResumenDTO> ocupadas,
                              List<String> horasLibres) {
        this.idPista = idPista;
        this.nombrePista = nombrePista;
        this.fecha = fecha;
        this.ocupadas = ocupadas;
        this.horasLibres = horasLibres;
    }

    public int getIdPista()                        { return idPista; }
    public String getNombrePista()                 { return nombrePista; }
    public LocalDate getFecha()                    { return fecha; }
    public List<ReservaResumenDTO> getOcupadas()    { return ocupadas; }
    public List<String> getHorasLibres()            { return horasLibres; }
}
