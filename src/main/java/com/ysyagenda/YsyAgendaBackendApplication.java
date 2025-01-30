package com.ysyagenda;


import com.ysyagenda.entity.*;
import com.ysyagenda.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@SpringBootApplication
public class YsyAgendaBackendApplication extends SpringBootServletInitializer { // Extender SpringBootServletInitializer
    public static void main(String[] args) {
        SpringApplication.run(YsyAgendaBackendApplication.class, args);
    }

    @Bean
    @Transactional
    public CommandLineRunner sampleData(
            UsuarioRepository usuarioRepository,
            TurnoRepository turnoRepository,
            EspecialidadRepository especialidadRepository,
            DisponibilidadRepository disponibilidadRepository) {
        return (args) -> {
            // Primero crear las especialidades

            Especialidad cardiologia = especialidadRepository.save(new Especialidad("Cardiología"));
            Especialidad neurologia = especialidadRepository.save(new Especialidad("Neurología"));
            Especialidad pediatria = especialidadRepository.save(new Especialidad("Pediatría"));

            //Crear 3 usuarios ADMIN
            usuarioRepository.save(new Usuario("Marian", "Van Staden", "000", "admin1@example.com", "000", Usuario.TipoUsuario.ADMIN));
            usuarioRepository.save(new Usuario("Admin2", "Garcia", "22222222", "admin2@example.com", "AdminPass2", Usuario.TipoUsuario.ADMIN));
            usuarioRepository.save(new Usuario("Admin3", "Lopez", "33333333", "admin3@example.com", "AdminPass3", Usuario.TipoUsuario.ADMIN));

            // Crear 3 usuarios PACIENTE
            usuarioRepository.save(new Usuario("Valen", "Leoz", "111", "paciente1@example.com", "111", Usuario.TipoUsuario.PACIENTE));
            usuarioRepository.save(new Usuario("Paciente2", "Fernandez", "55555555", "paciente2@example.com", "PacientePass2", Usuario.TipoUsuario.PACIENTE));
            usuarioRepository.save(new Usuario("Paciente3", "Rodriguez", "66666666", "paciente3@example.com", "PacientePass3", Usuario.TipoUsuario.PACIENTE));

            // Crear usuarios profesionales vinculados a las especialidades
            Usuario profesional1 = new Usuario("Martu", "Palleiro", "999", "profesional1@example.com", "999", Usuario.TipoUsuario.PROFESIONAL);
            profesional1.setEspecialidad(cardiologia.getNombre());
            usuarioRepository.save(profesional1);

            Usuario profesional2 = new Usuario("Profesional2", "Gomez", "88888888", "profesional2@example.com", "ProfesionalPass2", Usuario.TipoUsuario.PROFESIONAL);
            profesional2.setEspecialidad(neurologia.getNombre());
            usuarioRepository.save(profesional2);

            Usuario profesional3 = new Usuario("Profesional3", "Alvarez", "99999999", "profesional3@example.com", "ProfesionalPass3", Usuario.TipoUsuario.PROFESIONAL);
            profesional3.setEspecialidad(pediatria.getNombre());
            usuarioRepository.save(profesional3);
            Disponibilidad disponibilidad1 = new Disponibilidad();
            disponibilidad1.setUsuario(profesional1);
            disponibilidad1.setDiaSemana(Disponibilidad.DiaSemana.LUNES);
            disponibilidad1.setDisponible(true);
            disponibilidad1.setHorarioCortado(false);
            disponibilidad1.setFechaInicio(LocalDate.of(2025, 1, 1));
            disponibilidad1.setFechaFin(LocalDate.of(2025, 12, 31));
            disponibilidad1.setDuracionTurno(20);

            // Franjas para disponibilidad1
            FranjaHoraria franja1A = new FranjaHoraria();
            franja1A.setHoraInicio(LocalTime.of(9, 0));
            franja1A.setHoraFin(LocalTime.of(12, 0));

            FranjaHoraria franja1B = new FranjaHoraria();
            franja1B.setHoraInicio(LocalTime.of(14, 0));
            franja1B.setHoraFin(LocalTime.of(18, 0));

            disponibilidad1.addFranjaHoraria(franja1A);
            disponibilidad1.addFranjaHoraria(franja1B);

            // Disponibilidad 2
            Disponibilidad disponibilidad2 = new Disponibilidad();
            disponibilidad2.setUsuario(profesional2);
            disponibilidad2.setDiaSemana(Disponibilidad.DiaSemana.MIERCOLES);
            disponibilidad2.setDisponible(true);
            disponibilidad2.setHorarioCortado(true);
            disponibilidad2.setFechaInicio(LocalDate.of(2025, 1, 1));
            disponibilidad2.setFechaFin(LocalDate.of(2025, 12, 31));
            disponibilidad2.setDuracionTurno(15);
            // Franja para disponibilidad2
            FranjaHoraria franja2A = new FranjaHoraria();
            franja2A.setHoraInicio(LocalTime.of(9, 0));
            franja2A.setHoraFin(LocalTime.of(12, 0));

            disponibilidad2.addFranjaHoraria(franja2A);

            // Disponibilidad 3
            Disponibilidad disponibilidad3 = new Disponibilidad();
            disponibilidad3.setUsuario(profesional3);
            disponibilidad3.setDiaSemana(Disponibilidad.DiaSemana.VIERNES);
            disponibilidad3.setDisponible(true);
            disponibilidad3.setHorarioCortado(false);
            disponibilidad3.setFechaInicio(LocalDate.of(2025, 1, 1));
            disponibilidad3.setFechaFin(LocalDate.of(2025, 12, 31));
            disponibilidad1.setDuracionTurno(60);
            // Franja para disponibilidad3
            FranjaHoraria franja3A = new FranjaHoraria();
            franja3A.setHoraInicio(LocalTime.of(14, 0));
            franja3A.setHoraFin(LocalTime.of(18, 0));

            disponibilidad3.addFranjaHoraria(franja3A);

            // Guardar todas las disponibilidades
            disponibilidadRepository.saveAll(List.of(disponibilidad1, disponibilidad2, disponibilidad3));

            System.out.println("Mock data de disponibilidades y franjas horarias creadas con éxito.");
        };
    }
}