package com.titanium.practicingspring.model.DTOs;

import java.time.LocalDateTime;
import lombok.Data;

// DTO (Data Transfer Object) — objeto que usamos para enviar/recibir datos del usuario
// A diferencia de la entidad Usuario, este objeto NO tiene anotaciones JPA
// y no expone campos sensibles como la contraseña
@Data
public class UsuarioDTO {

    private String nombre;
    private String email;
    private String telefono;
    private String rol;
    private boolean activo;
    private LocalDateTime fechaCreacion;
}
