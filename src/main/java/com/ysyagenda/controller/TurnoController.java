package com.ysyagenda.controller;

import com.ysyagenda.entity.Turno;
import com.ysyagenda.repository.TurnoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/turnos")
public class TurnoController {
    private final TurnoRepository turnoRepository;

    public TurnoController(TurnoRepository turnoRepository) {
        this.turnoRepository = turnoRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Turno> getTurnos() {
        return turnoRepository.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Turno getTurno(@PathVariable long id) {
        return turnoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Turno con id %s no encontrado", id)));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Turno createTurno(@Valid @RequestBody Turno turno) {
        return turnoRepository.save(turno);
    }

    @DeleteMapping("/{id}")
    public void deleteTurno(@PathVariable long id) {
        turnoRepository.deleteById(id);
    }
}
