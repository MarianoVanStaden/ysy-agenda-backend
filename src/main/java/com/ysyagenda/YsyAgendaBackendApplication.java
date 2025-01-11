package com.ysyagenda;


import com.ysyagenda.entity.*;
import com.ysyagenda.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;


@SpringBootApplication
public class YsyAgendaBackendApplication extends SpringBootServletInitializer { // Extender SpringBootServletInitializer
    public static void main(String[] args) {
        SpringApplication.run(YsyAgendaBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner sampleData(
            UsuarioRepository usuarioRepository,
            TurnoRepository turnoRepository,
            EspecialidadRepository especialidadRepository) {
        return (args) -> {
            //Acá genero registros para poder testear las APIs

            // Guardar 5 pacientes
            usuarioRepository.save(new Usuario("Marian", "Van Staden", "000", "admin1@example.com", "000", Usuario.TipoUsuario.ADMIN));
            usuarioRepository.save(new Usuario("Admin2", "Garcia", "22222222", "admin2@example.com", "AdminPass2", Usuario.TipoUsuario.ADMIN));
            usuarioRepository.save(new Usuario("Admin3", "Lopez", "33333333", "admin3@example.com", "AdminPass3", Usuario.TipoUsuario.ADMIN));

            // Crear 3 usuarios PACIENTE
            usuarioRepository.save(new Usuario("Valen", "Leoz", "111", "paciente1@example.com", "111", Usuario.TipoUsuario.PACIENTE));
            usuarioRepository.save(new Usuario("Paciente2", "Fernandez", "55555555", "paciente2@example.com", "PacientePass2", Usuario.TipoUsuario.PACIENTE));
            usuarioRepository.save(new Usuario("Paciente3", "Rodriguez", "66666666", "paciente3@example.com", "PacientePass3", Usuario.TipoUsuario.PACIENTE));

            // Crear 3 usuarios PROFESIONAL
            Usuario profesional1 = new Usuario("Martu", "Palleiro", "999", "profesional1@example.com", "999", Usuario.TipoUsuario.PROFESIONAL);
            profesional1.setEspecialidad("Cardiología");

            Usuario profesional2 = new Usuario("Profesional2", "Gomez", "88888888", "profesional2@example.com", "ProfesionalPass2", Usuario.TipoUsuario.PROFESIONAL);
            profesional2.setEspecialidad("Neurología");

            Usuario profesional3 = new Usuario("Profesional3", "Alvarez", "99999999", "profesional3@example.com", "ProfesionalPass3", Usuario.TipoUsuario.PROFESIONAL);
            profesional3.setEspecialidad("Pediatría");

            usuarioRepository.save(profesional1);
            usuarioRepository.save(profesional2);
            usuarioRepository.save(profesional3);


            // Guardar 5 especialidades
            especialidadRepository.save(new Especialidad("Pediatría"));
            especialidadRepository.save(new Especialidad("Dermatología"));
            especialidadRepository.save(new Especialidad("Traumatología"));
            especialidadRepository.save(new Especialidad("Ginecología"));
            especialidadRepository.save(new Especialidad("Psiquiatría"));


            // Guardar 20 turnos (ejemplo usando fechas)
            //turnoRepository.save(new Turno(pacienteRepository.findByNombre("Mariano").get(0), doctorRepository.findByEspecialidad("Neurología").get(0), LocalDateTime.of(2024, 10, 5, 10, 0)));
           // turnoRepository.save(new Turno(pacienteRepository.findByNombre("Valentina").get(0), doctorRepository.findByEspecialidad("Cardiología").get(0), LocalDateTime.of(2024, 10, 6, 11, 0)));
          //  turnoRepository.save(new Turno(pacienteRepository.findByNombre("Rocio").get(0), doctorRepository.findByEspecialidad("Pediatría").get(0), LocalDateTime.of(2024, 10, 7, 12, 0)));
         //   turnoRepository.save(new Turno(pacienteRepository.findByNombre("Gonzalo").get(0), doctorRepository.findByEspecialidad("Dermatología").get(0), LocalDateTime.of(2024, 10, 8, 9, 0)));


        };
    }
}