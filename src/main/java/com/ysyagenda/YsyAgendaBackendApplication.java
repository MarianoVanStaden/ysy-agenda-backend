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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class YsyAgendaBackendApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(YsyAgendaBackendApplication.class, args);
    }

    @Bean
    @Transactional
    public CommandLineRunner sampleData(
            UsuarioRepository usuarioRepository,
            TurnoRepository turnoRepository,
            EspecialidadRepository especialidadRepository,
            DisponibilidadRepository disponibilidadRepository,
            ClinicaRepository clinicaRepository) {  // Agregar el repositorio de Clinica
        return (args) -> {
            // Crear la Clínica N°1
            Clinica clinica1 = new Clinica();
            clinica1.setNombre("Clínica N°1");
            clinica1.setDireccion("Av. Siempre Viva 123");
            clinica1.setTelefono("011-5555-5555");
            clinica1 = clinicaRepository.save(clinica1);  // Guardar en la base de datos

            // Crear especialidades disponibles en la Clínica N°1
            Especialidad cardiologia = especialidadRepository.save(new Especialidad("Cardiología"));
            Especialidad neurologia = especialidadRepository.save(new Especialidad("Neurología"));
            Especialidad pediatria = especialidadRepository.save(new Especialidad("Pediatría"));

            // Crear administradores de la Clínica N°1
            Usuario admin1 = new Usuario("Marian", "Van Staden", "000", "02235636363", "admin1@clinica1.com", "000", Usuario.TipoUsuario.ADMIN);
            admin1.setClinica(clinica1);
            usuarioRepository.save(admin1);

            Usuario admin2 = new Usuario("Admin2", "García", "22222222", "0115484784", "admin2@clinica1.com", "AdminPass2", Usuario.TipoUsuario.ADMIN);
            admin2.setClinica(clinica1);
            usuarioRepository.save(admin2);

            // Crear pacientes de la Clínica N°1
            Usuario paciente1 = new Usuario("Valen", "Leoz", "111", "02235789858", "paciente1@clinica1.com", "111", Usuario.TipoUsuario.PACIENTE);
            paciente1.setClinica(clinica1);
            usuarioRepository.save(paciente1);

            Usuario paciente2 = new Usuario("Paciente2", "Fernandez", "55555555", "2235616161", "paciente2@clinica1.com", "PacientePass2", Usuario.TipoUsuario.PACIENTE);
            paciente2.setClinica(clinica1);
            usuarioRepository.save(paciente2);

            // Crear profesionales y asignar especialidades
            Usuario profesional1 = new Usuario("Martu", "Palleiro", "999", "0223123456", "profesional1@clinica1.com", "999", Usuario.TipoUsuario.PROFESIONAL);
            profesional1.setEspecialidad(cardiologia.getNombre());
            profesional1.setClinica(clinica1);
            usuarioRepository.save(profesional1);

            Usuario profesional2 = new Usuario("Carlos", "Gómez", "88888888", "2235616161", "profesional2@clinica1.com", "ProfesionalPass2", Usuario.TipoUsuario.PROFESIONAL);
            profesional2.setEspecialidad(neurologia.getNombre());
            profesional2.setClinica(clinica1);
            usuarioRepository.save(profesional2);

            // Crear disponibilidades para los profesionales de la Clínica N°1
            Disponibilidad disponibilidad1 = new Disponibilidad();
            disponibilidad1.setUsuario(profesional1);
            disponibilidad1.setDiaSemana(Disponibilidad.DiaSemana.LUNES);
            disponibilidad1.setDisponible(true);
            disponibilidad1.setHorarioCortado(false);
            disponibilidad1.setFechaInicio(LocalDate.of(2025, 1, 1));
            disponibilidad1.setFechaFin(LocalDate.of(2025, 12, 31));
            disponibilidad1.setDuracionTurno(30);

            FranjaHoraria franja1A = new FranjaHoraria();
            franja1A.setHoraInicio(LocalTime.of(9, 0));
            franja1A.setHoraFin(LocalTime.of(17, 0));
            disponibilidad1.addFranjaHoraria(franja1A);

            Disponibilidad disponibilidad2 = new Disponibilidad();
            disponibilidad2.setUsuario(profesional2);
            disponibilidad2.setDiaSemana(Disponibilidad.DiaSemana.MIERCOLES);
            disponibilidad2.setDisponible(true);
            disponibilidad2.setHorarioCortado(true);
            disponibilidad2.setFechaInicio(LocalDate.of(2025, 1, 1));
            disponibilidad2.setFechaFin(LocalDate.of(2025, 12, 31));
            disponibilidad2.setDuracionTurno(20);

            FranjaHoraria franja2A = new FranjaHoraria();
            franja2A.setHoraInicio(LocalTime.of(8, 30));
            franja2A.setHoraFin(LocalTime.of(14, 0));
            disponibilidad2.addFranjaHoraria(franja2A);

            disponibilidadRepository.saveAll(List.of(disponibilidad1, disponibilidad2));
            System.out.println("Mock data de Clínica N°1 creada con éxito.");
        };
    }
}
