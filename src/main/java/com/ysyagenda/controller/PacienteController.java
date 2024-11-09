package com.ysyagenda.controller;

import com.ysyagenda.service.EmailService;
import org.springframework.http.HttpStatus;
import com.ysyagenda.entity.Paciente;
import com.ysyagenda.repository.PacienteRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteRepository pacienteRepository;

    @Autowired
    private EmailService emailService;  // Inyectamos el servicio de envío de correo

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

    // Nuevo método para enviar la contraseña por correo electrónico
    @PostMapping("/recuperar-contraseña")
    public String recuperarContraseña(@RequestParam String email) {
        try {
            Paciente paciente = pacienteRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Paciente con email %s no encontrado", email)));

            // Enviar un correo con la contraseña
            String subject = "Recuperación de contraseña";
            String body = "Estimado/a " + paciente.getNombre() + ",\n\nTu contraseña es: " + paciente.getContraseña();
            emailService.sendEmail(paciente.getEmail(), subject, body);

            return "Correo enviado a " + paciente.getEmail();
        } catch (ResponseStatusException e) {
            throw e; // Lanzar de nuevo la excepción para que Spring la maneje
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al recuperar la contraseña", e);
        }
    }


    // Método para actualizar solo la contraseña
    @PatchMapping(value = "/{id}/contraseña", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Paciente updateContraseña(@PathVariable long id, @RequestBody String nuevaContraseña) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Paciente con id %s no encontrado", id)));

        paciente.setContraseña(nuevaContraseña);
        return pacienteRepository.save(paciente);
    }
}
