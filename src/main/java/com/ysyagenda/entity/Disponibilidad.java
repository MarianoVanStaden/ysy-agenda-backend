// Disponibilidad.java (entity)
package com.ysyagenda.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "disponibilidades")
public class Disponibilidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private Integer diaSemana;

    @Column(nullable = false)
    private boolean disponible;

    @Column(nullable = false)
    private boolean horarioCortado;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "disponibilidad_id")
    private List<FranjaHoraria> franjasHorarias;
}