package com.ysyagenda.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Paciente {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotEmpty String getNombre() {
        return nombre;
    }

    public void setNombre(@NotEmpty String nombre) {
        this.nombre = nombre;
    }

    public @NotEmpty String getApellido() {
        return apellido;
    }

    public void setApellido(@NotEmpty String apellido) {
        this.apellido = apellido;
    }

    public @NotEmpty String getDni() {
        return dni;
    }

    public void setDni(@NotEmpty String dni) {
        this.dni = dni;
    }

    public @Email @NotEmpty String getEmail() {
        return email;
    }

    public void setEmail(@Email @NotEmpty String email) {
        this.email = email;
    }

    public @NotEmpty String getContraseña() {
        return contraseña;
    }

    public Paciente(String nombre, String apellido, String dni, String email, String contraseña) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.contraseña = contraseña;
    }

    // Método para actualizar solo la contraseña
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}
