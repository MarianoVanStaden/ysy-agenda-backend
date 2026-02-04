package com.ysyagenda.repository;

import com.ysyagenda.entity.Turno;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TurnoRepository extends CrudRepository<Turno, Long> {
    List<Turno> findByFechaBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<Turno> findByProfesionalId(Long profesionalId);
    boolean existsByProfesionalIdAndFecha(Long profesionalId, LocalDateTime fecha);
}

