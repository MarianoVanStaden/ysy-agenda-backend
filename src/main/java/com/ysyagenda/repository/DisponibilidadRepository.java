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

    List<Disponibilidad> findByUsuarioIdAndFechaInicioBetweenAndFechaFinBetween(
            Long usuarioId,
            LocalDate fechaInicioRango,
            LocalDate fechaFinRango,
            LocalDate fechaInicioRango2,
            LocalDate fechaFinRango2
    );

    @Query("SELECT d FROM Disponibilidad d WHERE d.usuario.id = :usuarioId " +
            "AND d.fechaInicio <= :fecha AND d.fechaFin >= :fecha " +
            "AND d.diaSemana = :diaSemana")
    List<Disponibilidad> findDisponibilidadPorDia(
            @Param("usuarioId") Long usuarioId,
            @Param("fecha") LocalDate fecha,
            @Param("diaSemana") Integer diaSemana
    );
}
