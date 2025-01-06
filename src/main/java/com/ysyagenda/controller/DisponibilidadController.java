package com.ysyagenda.controller;

import com.ysyagenda.entity.Disponibilidad;
import com.ysyagenda.entity.Usuario;
import com.ysyagenda.repository.DisponibilidadRepository;
import com.ysyagenda.repository.UsuarioRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/disponibilidad")
@CrossOrigin(origins = "*")
public class DisponibilidadController {

    private final DisponibilidadRepository disponibilidadRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public DisponibilidadController(DisponibilidadRepository disponibilidadRepository,
                                    UsuarioRepository usuarioRepository) {
        this.disponibilidadRepository = disponibilidadRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<Disponibilidad>> getDisponibilidadUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(disponibilidadRepository.findByUsuarioId(usuarioId));
    }

    @PostMapping
    public ResponseEntity<Disponibilidad> createDisponibilidad(@RequestBody DisponibilidadDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setUsuario(usuario);
        disponibilidad.setDiaSemana(dto.getDiaSemana());
        disponibilidad.setHoraInicio(dto.getHoraInicio());
        disponibilidad.setHoraFin(dto.getHoraFin());
        disponibilidad.setDisponible(dto.isDisponible());
        disponibilidad.setFechaInicio(dto.getFechaInicio());
        disponibilidad.setFechaFin(dto.getFechaFin());

        return ResponseEntity.ok(disponibilidadRepository.save(disponibilidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisponibilidad(@PathVariable Long id) {
        disponibilidadRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Disponibilidad> updateDisponibilidad(
            @PathVariable Long id,
            @RequestBody DisponibilidadDTO dto) {

        Disponibilidad disponibilidad = disponibilidadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disponibilidad no encontrada"));

        disponibilidad.setHoraInicio(dto.getHoraInicio());
        disponibilidad.setHoraFin(dto.getHoraFin());
        disponibilidad.setDisponible(dto.isDisponible());
        disponibilidad.setFechaInicio(dto.getFechaInicio());
        disponibilidad.setFechaFin(dto.getFechaFin());

        return ResponseEntity.ok(disponibilidadRepository.save(disponibilidad));
    }
}

