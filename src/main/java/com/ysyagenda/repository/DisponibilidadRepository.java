// DisponibilidadRepository.java
package com.ysyagenda.repository;

import com.ysyagenda.entity.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {
    List<Disponibilidad> findByUsuarioId(Long usuarioId);

    // No necesitamos incluir duracionTurno en el método ya que no es parte del criterio de búsqueda
    List<Disponibilidad> findByUsuarioIdAndDiaSemanaAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(
            Long usuarioId,
            Disponibilidad.DiaSemana diaSemana,
            LocalDate fechaFin,
            LocalDate fechaInicio
    );
}