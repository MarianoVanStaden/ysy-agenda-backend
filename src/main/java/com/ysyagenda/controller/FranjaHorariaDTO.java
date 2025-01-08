// FranjaHorariaDTO.java
package com.ysyagenda.controller;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class FranjaHorariaDTO {
    private LocalTime horaInicio;
    private LocalTime horaFin;
}