package com.ysyagenda.controller;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

// DTO para transferencia de datos
@Data
public class DisponibilidadDTO {
    private Long usuarioId;
    private Integer diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private boolean disponible;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
