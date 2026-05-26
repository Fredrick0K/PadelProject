package com.titanium.practicingspring.model.DTOs;

import java.time.LocalDateTime;
import lombok.Data;

// Se usan DTOs para definir la estructura de los datos que se envían o reciben en los endpoints.
// No tienen lógica de negocio, solo getters y setters. 
// Es una buena práctica usar DTOs para no exponer las entidades de la base de datos.
// Aqui no se expone la contraseña ni el ID, ya que no es necesario para mostrar la info de un usuario en una lista,
// ademas de que no nos gustaria exponer la contraseña.
@Data
public class UsuarioDTO {

    private String nombre;
    private String email;
    private String telefono;
    private String rol;
    private boolean activo;
    private LocalDateTime fechaCreacion;
}
