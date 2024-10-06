package com.ysyagenda.controller;

import org.springframework.http.HttpStatus;
import com.ysyagenda.entity.Paciente;
import com.ysyagenda.repository.PacienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
    private final PacienteRepository pacienteRepository;

    public PacienteController(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Paciente> getPacientes() {
        return pacienteRepository.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Paciente getPaciente(@PathVariable long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Paciente con id %s no encontrado", id)));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Paciente createPaciente(@Valid @RequestBody Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    @DeleteMapping("/{id}")
    public void deletePaciente(@PathVariable long id) {
        pacienteRepository.deleteById(id);
    }

    // Nuevo método para actualizar solo la contraseña
    @PatchMapping(value = "/{id}/contraseña", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Paciente updateContraseña(@PathVariable long id, @RequestBody String nuevaContraseña) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Paciente con id %s no encontrado", id)));

        paciente.setContraseña(nuevaContraseña);
        return pacienteRepository.save(paciente);
    }
}

