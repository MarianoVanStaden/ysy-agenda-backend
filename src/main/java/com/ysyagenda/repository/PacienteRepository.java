package com.ysyagenda.repository;

import com.ysyagenda.entity.Paciente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacienteRepository extends CrudRepository<Paciente, Long> {
    List<Paciente> findByNombre(String nombre);
}
