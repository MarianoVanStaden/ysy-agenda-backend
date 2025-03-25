package com.ysyagenda.controller;

import com.ysyagenda.entity.Usuario;
import com.ysyagenda.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> loginRequest) {
        String dni = (String) loginRequest.get("dni");
        String password = (String) loginRequest.get("password");
        Long clinicaId = ((Number) loginRequest.get("clinicaId")).longValue();

        Usuario usuario = usuarioRepository.findByDniAndClinicaId(dni, clinicaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "DNI o contraseña incorrectos"));

        if (!usuario.getContrasenia().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "DNI o contraseña incorrectos");
        }

        return ResponseEntity.ok(usuario);
    }
}
