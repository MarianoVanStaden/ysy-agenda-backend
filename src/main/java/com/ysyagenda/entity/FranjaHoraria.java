package com.ysyagenda.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "franjas_horarias")
public class FranjaHoraria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFin;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "disponibilidad_id", nullable = false)
    private Disponibilidad disponibilidad;

    public void setHoraInicio(LocalTime horaInicio) {
        if (horaFin != null && horaInicio.isAfter(horaFin)) {
            throw new IllegalArgumentException("La hora de inicio no puede ser después de la hora de fin.");
        }
        this.horaInicio = horaInicio;
    }

    public void setHoraFin(LocalTime horaFin) {
        if (horaInicio != null && horaFin.isBefore(horaInicio)) {
            throw new IllegalArgumentException("La hora de fin no puede ser antes de la hora de inicio.");
        }
        this.horaFin = horaFin;
    }
}
