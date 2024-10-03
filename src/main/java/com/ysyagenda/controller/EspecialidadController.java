package com.ysyagenda.controller;

import com.ysyagenda.entity.Especialidad;
import com.ysyagenda.repository.EspecialidadRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/especialidades")
public class EspecialidadController {
    private final EspecialidadRepository especialidadRepository;

    public EspecialidadController(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Especialidad> getEspecialidades() {
        return especialidadRepository.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Especialidad getEspecialidad(@PathVariable long id) {
        return especialidadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Especialidad con id %s no encontrada", id)));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Especialidad createEspecialidad(@Valid @RequestBody Especialidad especialidad) {
        return especialidadRepository.save(especialidad);
    }

    @DeleteMapping("/{id}")
    public void deleteEspecialidad(@PathVariable long id) {
        especialidadRepository.deleteById(id);
    }
}
