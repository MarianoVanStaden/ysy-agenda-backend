package com.ysyagenda.repository;

import com.ysyagenda.entity.Especialidad;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspecialidadRepository extends CrudRepository<Especialidad, Long> {
    List<Especialidad> findByNombre(String nombre);
}
