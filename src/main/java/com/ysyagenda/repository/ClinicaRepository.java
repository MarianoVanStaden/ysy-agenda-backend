package com.ysyagenda.repository;

import com.ysyagenda.entity.Clinica;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicaRepository extends CrudRepository<Clinica, Long> {
}
