package com.ysyagenda.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    private String nombre;

    @NotEmpty
    private String apellido;

    @NotEmpty
    private String dni;

    @NotEmpty
    private String celular;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String contrasenia;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    // Campos específicos por tipo
    private String departamento;  // Para ADMIN
    private String especialidad;  // Para PROFESIONAL
    private String avatarColor; //Para guardar el color del Avatar

    public enum TipoUsuario {
        ADMIN, PACIENTE, PROFESIONAL
    }

    public Usuario(String nombre, String apellido, String dni, String celular, String email,
                   String contrasenia, TipoUsuario tipoUsuario) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.celular = celular;
        this.email = email;
        this.contrasenia = contrasenia;
        this.tipoUsuario = tipoUsuario;
    }
    public String getAvatarText() {
        if (nombre != null && apellido != null) {
            return nombre.substring(0, 1).toUpperCase() + apellido.substring(0, 1).toUpperCase();
        }
        return "??";
    }

}