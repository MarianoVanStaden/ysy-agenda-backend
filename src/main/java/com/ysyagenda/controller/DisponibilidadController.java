package com.ysyagenda.controller;

import com.ysyagenda.entity.Disponibilidad;
import com.ysyagenda.entity.Usuario;
import com.ysyagenda.entity.FranjaHoraria;
import com.ysyagenda.repository.DisponibilidadRepository;
import com.ysyagenda.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/disponibilidad")
@CrossOrigin(origins = "http://localhost:8081")
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

        if (usuario.getTipoUsuario() != Usuario.TipoUsuario.PROFESIONAL) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Solo los usuarios de tipo PROFESIONAL pueden tener disponibilidades.");
        }

        List<Disponibilidad> disponibilidadesExistentes = disponibilidadRepository
                .findByUsuarioIdAndDiaSemanaAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(
                        dto.getUsuarioId(),
                        dto.getDiaSemana(),
                        dto.getFechaFin(),
                        dto.getFechaInicio()
                );

        if (!disponibilidadesExistentes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ya existe disponibilidad para este día en el rango de fechas especificado.");
        }

        validateFranjasHorarias(dto);

        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setUsuario(usuario);
        disponibilidad.setDiaSemana(dto.getDiaSemana());
        disponibilidad.setDisponible(dto.isDisponible());
        disponibilidad.setHorarioCortado(dto.isHorarioCortado());
        disponibilidad.setFechaInicio(dto.getFechaInicio());
        disponibilidad.setFechaFin(dto.getFechaFin());

        List<FranjaHoraria> franjas = mapFranjas(dto);
        franjas.forEach(disponibilidad::addFranjaHoraria);

        return ResponseEntity.ok(disponibilidadRepository.save(disponibilidad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Disponibilidad> updateDisponibilidad(
            @PathVariable Long id,
            @RequestBody DisponibilidadDTO dto) {
        Disponibilidad disponibilidad = disponibilidadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Disponibilidad no encontrada"));

        validateFranjasHorarias(dto);

        disponibilidad.setDisponible(dto.isDisponible());
        disponibilidad.setHorarioCortado(dto.isHorarioCortado());
        disponibilidad.setFechaInicio(dto.getFechaInicio());
        disponibilidad.setFechaFin(dto.getFechaFin());

        disponibilidad.getFranjasHorarias().clear();
        List<FranjaHoraria> franjas = mapFranjas(dto);
        franjas.forEach(disponibilidad::addFranjaHoraria);

        return ResponseEntity.ok(disponibilidadRepository.save(disponibilidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisponibilidad(@PathVariable Long id) {
        if (!disponibilidadRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Disponibilidad no encontrada");
        }
        disponibilidadRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void validateFranjasHorarias(DisponibilidadDTO dto) {
        if (dto.getFranjasHorarias() == null || dto.getFranjasHorarias().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debe especificar al menos una franja horaria.");
        }

        if (dto.isHorarioCortado() && dto.getFranjasHorarias().size() != 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El horario cortado debe tener exactamente dos franjas horarias.");
        }

        if (!dto.isHorarioCortado() && dto.getFranjasHorarias().size() > 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El horario continuo debe tener una sola franja horaria.");
        }

        for (FranjaHorariaDTO franjaDTO : dto.getFranjasHorarias()) {
            if (franjaDTO.getHoraFin().isBefore(franjaDTO.getHoraInicio())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La hora de fin debe ser posterior a la hora de inicio.");
            }
        }
    }

    private List<FranjaHoraria> mapFranjas(DisponibilidadDTO dto) {
        return dto.getFranjasHorarias().stream()
                .map(franjaDTO -> {
                    FranjaHoraria franja = new FranjaHoraria();
                    franja.setHoraInicio(franjaDTO.getHoraInicio());
                    franja.setHoraFin(franjaDTO.getHoraFin());
                    return franja;
                })
                .collect(Collectors.toList());
    }
}
