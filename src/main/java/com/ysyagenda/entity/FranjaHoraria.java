// FranjaHoraria.java (entity)
package com.ysyagenda.entity;

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
}