package com.ysyagenda.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "disponibilidades")
public class Disponibilidad {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disponibilidad_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaSemana diaSemana;

    @Column(nullable = false)
    private boolean disponible;

    @Column(nullable = false)
    private boolean horarioCortado;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    @Column(nullable = false)
    private Integer duracionTurno = 30;

    @JsonManagedReference
    @OneToMany(mappedBy = "disponibilidad",
            cascade = {CascadeType.ALL},
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<FranjaHoraria> franjasHorarias = new ArrayList<>();

    public void setUsuario(Usuario usuario) {
        if (usuario.getTipoUsuario() != Usuario.TipoUsuario.PROFESIONAL) {
            throw new IllegalArgumentException("La disponibilidad solo puede asociarse a un usuario de tipo PROFESIONAL.");
        }
        this.usuario = usuario;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        if (fechaFin != null && fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }
        this.fechaFin = fechaFin;
    }

    public void addFranjaHoraria(FranjaHoraria franjaHoraria) {
        franjaHoraria.setDisponibilidad(this);
        this.franjasHorarias.add(franjaHoraria);
    }

    public void removeFranjaHoraria(FranjaHoraria franjaHoraria) {
        this.franjasHorarias.remove(franjaHoraria);
        franjaHoraria.setDisponibilidad(null);
    }

    public enum DiaSemana {
        LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO
    }

    public void setDuracionTurno(Integer duracionTurno) {
        if (duracionTurno <= 0) {
            throw new IllegalArgumentException("La duración del turno debe ser mayor a 0 minutos.");
        }
        this.duracionTurno = duracionTurno;
    }
}
