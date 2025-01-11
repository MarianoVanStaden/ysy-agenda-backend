package com.ysyagenda.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Usuario paciente;

    @ManyToOne
    @JoinColumn(name = "profesional_id", nullable = false)
    private Usuario profesional;

    @NotNull
    private LocalDateTime fecha;

    public Turno(Usuario paciente, Usuario profesional, LocalDateTime fecha) {
        if (paciente.getTipoUsuario() != Usuario.TipoUsuario.PACIENTE) {
            throw new IllegalArgumentException("El usuario paciente debe ser de tipo PACIENTE");
        }
        if (profesional.getTipoUsuario() != Usuario.TipoUsuario.PROFESIONAL) {
            throw new IllegalArgumentException("El usuario profesional debe ser de tipo PROFESIONAL");
        }

        this.paciente = paciente;
        this.profesional = profesional;
        this.fecha = fecha;
    }

    // Getters y setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setPaciente(Usuario paciente) {
        if (paciente.getTipoUsuario() != Usuario.TipoUsuario.PACIENTE) {
            throw new IllegalArgumentException("El usuario paciente debe ser de tipo PACIENTE");
        }
        this.paciente = paciente;
    }

    public void setProfesional(Usuario profesional) {
        if (profesional.getTipoUsuario() != Usuario.TipoUsuario.PROFESIONAL) {
            throw new IllegalArgumentException("El usuario profesional debe ser de tipo PROFESIONAL");
        }
        this.profesional = profesional;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
