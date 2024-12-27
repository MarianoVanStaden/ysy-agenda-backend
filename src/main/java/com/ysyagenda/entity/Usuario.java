package com.ysyagenda.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
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

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String contraseña;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    // Campos específicos por tipo
    private String departamento;  // Para ADMIN
    private String especialidad;  // Para PROFESIONAL

    public enum TipoUsuario {
        ADMIN, PACIENTE, PROFESIONAL
    }

    public Usuario(String nombre, String apellido, String dni, String email,
                   String contraseña, TipoUsuario tipoUsuario) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.contraseña = contraseña;
        this.tipoUsuario = tipoUsuario;
    }

    // Getters y setters
    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setDni(String dni) { this.dni = dni; }
    public void setEmail(String email) { this.email = email; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
    public void setTipoUsuario(TipoUsuario tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public Long getId() {
        return id;
    }

    public @NotEmpty String getNombre() {
        return nombre;
    }

    public @NotEmpty String getApellido() {
        return apellido;
    }

    public @NotEmpty String getDni() {
        return dni;
    }

    public @Email @NotEmpty String getEmail() {
        return email;
    }

    public @NotEmpty String getContraseña() {
        return contraseña;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public String getDepartamento() {
        return departamento;
    }

    public String getEspecialidad() {
        return especialidad;
    }
}