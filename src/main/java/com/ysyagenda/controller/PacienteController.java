package com.ysyagenda.controller;

import com.ysyagenda.service.EmailService;
import com.ysyagenda.entity.Paciente;
import com.ysyagenda.repository.PacienteRepository;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteRepository pacienteRepository;
    private final EmailService emailService;

    @Autowired
    public PacienteController(PacienteRepository pacienteRepository, EmailService emailService) {
        this.pacienteRepository = pacienteRepository;
        this.emailService = emailService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Paciente> getPacientes() {
        return pacienteRepository.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Paciente getPaciente(@PathVariable long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Paciente con id %s no encontrado", id)));
    }
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Paciente updatePaciente(@PathVariable long id, @RequestBody Map<String, Object> updates) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"));

        // Verificamos y actualizamos los campos enviados en el cuerpo de la solicitud
        updates.forEach((key, value) -> {
            switch (key) {
                case "nombre":
                    paciente.setNombre((String) value);
                    break;
                case "apellido":
                    paciente.setApellido((String) value);
                    break;
                case "email":
                    paciente.setEmail((String) value);
                    break;
                case "dni":
                    paciente.setDni((String) value);
                    break;
            }
        });

        return pacienteRepository.save(paciente); // Guardamos los cambios en la base de datos
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Paciente createPaciente(@Valid @RequestBody Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    @DeleteMapping("/{id}")
    public void deletePaciente(@PathVariable long id) {
        pacienteRepository.deleteById(id);
    }

    @PostMapping("/recuperar-contraseña")
    public ResponseEntity<Map<String, String>> recuperarContraseña(@RequestParam String email) {
        try {
            Paciente paciente = pacienteRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            String.format("Paciente con email %s no encontrado", email)));

            // Enviar la contraseña directamente por correo
            emailService.sendPasswordResetEmail(
                    paciente.getEmail(),
                    "Recuperación de contraseña",
                    buildPasswordEmailBody(paciente.getNombre(), paciente.getContraseña())
            );
            Map<String, String> response = new HashMap<>();
            response.put("message", "Se ha enviado la contraseña a tu correo");
            return ResponseEntity.ok(response);

        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Múltiples cuentas encontradas para el correo proporcionado",
                    e);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al procesar la solicitud",
                    e);
        }
    }

    private String buildPasswordEmailBody(String nombre, String contraseña) {
        return String.format(
                "Estimado/a %s,\n\n" +
                        "Tu contraseña es: %s\n\n" +
                        "Si no solicitaste este cambio, ignora este correo.",
                nombre, contraseña);
    }
}
