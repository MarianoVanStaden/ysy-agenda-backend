package com.ysyagenda.controller;

import com.ysyagenda.entity.Disponibilidad;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// DTO para transferencia de datos
@Getter
@Setter
@NoArgsConstructor
public class DisponibilidadDTO {
    private Long usuarioId;
    private Disponibilidad.DiaSemana diaSemana;
    private boolean disponible;
    private boolean horarioCortado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private List<FranjaHorariaDTO> franjasHorarias;
}