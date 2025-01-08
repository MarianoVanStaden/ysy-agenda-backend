package com.ysyagenda.repository;

import com.ysyagenda.entity.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {
    List<Disponibilidad> findByUsuarioId(Long usuarioId);

    List<Disponibilidad> findByUsuarioIdAndDiaSemanaAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(
            Long usuarioId,
            Integer diaSemana,
            LocalDate fechaFin,
            LocalDate fechaInicio
    );
}
