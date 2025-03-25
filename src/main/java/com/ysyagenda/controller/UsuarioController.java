package com.ysyagenda.controller;

import com.ysyagenda.service.EmailService;
import com.ysyagenda.entity.Usuario;
import com.ysyagenda.repository.UsuarioRepository;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;

    @Autowired
    public UsuarioController(UsuarioRepository usuarioRepository, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Usuario> getUsuarios(
            @RequestParam Long clinicaId,
            @RequestParam(required = false) Usuario.TipoUsuario tipo) {
        return tipo == null ? usuarioRepository.findByClinicaId(clinicaId) :
                usuarioRepository.findByTipoUsuarioAndClinicaId(tipo, clinicaId);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Usuario getUsuario(@PathVariable long id, @RequestParam Long clinicaId) {
        return usuarioRepository.findById(id)
                .filter(u -> u.getClinica().getId().equals(clinicaId))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado en la clínica especificada"));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Usuario createUsuario(@Valid @RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Usuario updateUsuario(@PathVariable long id, @RequestParam Long clinicaId, @RequestBody Map<String, Object> updates) {
        Usuario usuario = usuarioRepository.findById(id)
                .filter(u -> u.getClinica().getId().equals(clinicaId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "nombre": usuario.setNombre((String) value); break;
                case "apellido": usuario.setApellido((String) value); break;
                case "email": usuario.setEmail((String) value); break;
                case "dni": usuario.setDni((String) value); break;
                case "celular": usuario.setCelular((String) value); break;
                case "departamento": usuario.setDepartamento((String) value); break;
                case "especialidad": usuario.setEspecialidad((String) value); break;
                case "avatarColor": usuario.setAvatarColor((String) value); break;
            }
        });

        return usuarioRepository.save(usuario);
    }

    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable long id, @RequestParam Long clinicaId) {
        Usuario usuario = usuarioRepository.findById(id)
                .filter(u -> u.getClinica().getId().equals(clinicaId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        usuarioRepository.delete(usuario);
    }

    @PostMapping("/recuperar-contrasenia")
    public ResponseEntity<Map<String, String>> recuperarContrasenia(@RequestParam String email, @RequestParam Long clinicaId) {
        try {
            Usuario usuario = usuarioRepository.findByEmailAndClinicaId(email, clinicaId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

            emailService.sendPasswordResetEmail(
                    usuario.getEmail(),
                    "Recuperación de contraseña",
                    buildPasswordEmailBody(usuario.getNombre(), usuario.getContrasenia())
            );

            Map<String, String> response = new HashMap<>();
            response.put("message", "Se ha enviado la contraseña a tu correo");
            return ResponseEntity.ok(response);

        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Múltiples cuentas encontradas", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en la solicitud", e);
        }
    }

    private String buildPasswordEmailBody(String nombre, String contrasenia) {
        return String.format(
                "Estimado/a %s,\n\nTu contraseña es: %s\n\nSi no solicitaste este cambio, ignora este correo.",
                nombre, contrasenia);
    }
}
