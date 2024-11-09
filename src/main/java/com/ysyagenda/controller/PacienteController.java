package com.ysyagenda.controller;

import com.ysyagenda.service.MailgunService;
import com.ysyagenda.entity.PasswordResetToken;
import com.ysyagenda.repository.PasswordResetTokenRepository;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import com.ysyagenda.entity.Paciente;
import com.ysyagenda.repository.PacienteRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteRepository pacienteRepository;
    private final PasswordResetTokenRepository tokenRepository;

    @Autowired
    private MailgunService emailService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public PacienteController(PacienteRepository pacienteRepository,
                              PasswordResetTokenRepository tokenRepository) {
        this.pacienteRepository = pacienteRepository;
        this.tokenRepository = tokenRepository;
    }

    // Existing methods remain unchanged
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

    // Updated password recovery method
    @PostMapping("/recuperar-contraseña")
    public ResponseEntity<?> recuperarContraseña(@RequestParam String email) {
        try {
            Paciente paciente = pacienteRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            String.format("Paciente con email %s no encontrado", email)));

            // Generate token and save it
            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = new PasswordResetToken(token, paciente);
            tokenRepository.save(resetToken);

            // Create reset link
            String resetUrl = frontendUrl + "/reset-password?token=" + token;

            // Send email with reset link
            String subject = "Recuperación de contraseña";
            String body = String.format(
                    "Estimado/a %s,\n\n" +
                            "Has solicitado restablecer tu contraseña. Haz clic en el siguiente enlace:\n\n%s\n\n" +
                            "Este enlace expirará en 24 horas.\n\n" +
                            "Si no solicitaste este cambio, ignora este correo.",
                    paciente.getNombre(), resetUrl);

            emailService.sendSimpleMessage(paciente.getEmail(), subject, body);

            return ResponseEntity.ok()
                    .body(Map.of("message", "Se ha enviado un enlace de recuperación a tu correo"));

        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Multiple accounts found for the provided email", e);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al procesar la solicitud", e);
        }
    }

    // Updated password update method
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String nuevaContraseña) {
        try {
            PasswordResetToken resetToken = tokenRepository.findByToken(token);

            if (resetToken == null || resetToken.isExpired()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Token inválido o expirado"));
            }

            Paciente paciente = resetToken.getPaciente();
            paciente.setContraseña(BCrypt.hashpw(nuevaContraseña, BCrypt.gensalt()));
            pacienteRepository.save(paciente);

            // Delete the used token
            tokenRepository.delete(resetToken);

            return ResponseEntity.ok()
                    .body(Map.of("message", "Contraseña actualizada correctamente"));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al actualizar la contraseña", e);
        }
    }

    // Removing the old updateContraseña method as it's being replaced by the more secure reset-password endpoint
}