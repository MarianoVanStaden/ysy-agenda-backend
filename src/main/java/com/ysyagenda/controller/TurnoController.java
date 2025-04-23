package com.ysyagenda.controller;

import com.ysyagenda.entity.Turno;
import com.ysyagenda.repository.TurnoRepository;
import com.ysyagenda.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/turnos")
public class TurnoController {
    private final TurnoRepository turnoRepository;
    private final EmailService emailService;

    @Autowired
    public TurnoController(TurnoRepository turnoRepository, EmailService emailService) {
        this.turnoRepository = turnoRepository;
        this.emailService = emailService;
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
        // Save the turno
        Turno savedTurno = turnoRepository.save(turno);

        try {
            // Send confirmation email to patient
            // The patient data is already embedded in the turno object
            emailService.sendPasswordResetEmail(
                    savedTurno.getPaciente().getEmail(),
                    "Confirmación de Turno - YSY Agenda",
                    buildTurnoEmailBody(savedTurno)
            );
        } catch (Exception e) {
            // Log the error but don't stop the operation
            System.err.println("Error al enviar email de confirmación: " + e.getMessage());
            // Consider adding proper logging here
        }

        return savedTurno;
    }

    @DeleteMapping("/{id}")
    public void deleteTurno(@PathVariable long id) {
        turnoRepository.deleteById(id);
    }

    @GetMapping(value = "/profesional/{profesionalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Turno> getTurnosByProfesional(@PathVariable long profesionalId) {
        return turnoRepository.findByProfesionalId(profesionalId);
    }

    private String buildTurnoEmailBody(Turno turno) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        return String.format(
                "Estimado/a %s %s,\n\n" +
                        "Confirmamos tu turno:\n\n" +
                        "Fecha y Hora: %s\n" +
                        "Profesional: %s %s\n" +
                        "Especialidad: %s\n\n" +
                        "Gracias por utilizar YSY Agenda.\n" +
                        "Si necesitas cancelar o reprogramar, por favor contacta con nosotros.",
                turno.getPaciente().getNombre(),
                turno.getPaciente().getApellido(),
                turno.getFecha().format(formatter),
                turno.getProfesional().getNombre(),
                turno.getProfesional().getApellido(),
                turno.getProfesional().getEspecialidad()
        );
    }
}