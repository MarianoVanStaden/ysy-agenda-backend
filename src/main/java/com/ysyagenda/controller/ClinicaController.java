package com.ysyagenda.controller;

import com.ysyagenda.entity.Clinica;
import com.ysyagenda.repository.ClinicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/clinicas")
public class ClinicaController {

    private final ClinicaRepository clinicaRepository;

    @Autowired
    public ClinicaController(ClinicaRepository clinicaRepository) {
        this.clinicaRepository = clinicaRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Clinica> getClinicas() {
        return clinicaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Clinica getClinica(@PathVariable Long id) {
        return clinicaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clínica no encontrada"));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Clinica createClinica(@Valid @RequestBody Clinica clinica) {
        return clinicaRepository.save(clinica);
    }

    @PatchMapping("/{id}")
    public Clinica updateClinica(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Clinica clinica = clinicaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clínica no encontrada"));

        updates.forEach((key, value) -> {
            if ("nombre".equals(key)) clinica.setNombre((String) value);
            if ("direccion".equals(key)) clinica.setDireccion((String) value);
        });

        return clinicaRepository.save(clinica);
    }

    @DeleteMapping("/{id}")
    public void deleteClinica(@PathVariable Long id) {
        clinicaRepository.deleteById(id);
    }
}

