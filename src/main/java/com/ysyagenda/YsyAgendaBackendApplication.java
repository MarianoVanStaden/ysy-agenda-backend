package com.ysyagenda;


import com.ysyagenda.entity.Doctor;
import com.ysyagenda.entity.Especialidad;
import com.ysyagenda.entity.Paciente;
import com.ysyagenda.entity.Turno;
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
            PacienteRepository pacienteRepository,
            DoctorRepository doctorRepository,
            TurnoRepository turnoRepository,
            EspecialidadRepository especialidadRepository) {
        return (args) -> {
            //Acá genero registros para poder testear las APIs

            // Guardar 5 pacientes
            pacienteRepository.save(new Paciente("Martina", "Palleiro", "12345678", "martu@gmail.com", "Marti2024"));
            pacienteRepository.save(new Paciente("Rocio", "Villanueva", "23456789", "rovillanueva@yahoo.com", "Ro2024"));
            pacienteRepository.save(new Paciente("Gonzalo", "Seitz", "23456789", "gonza@idra.com", "Gonza2024"));
            pacienteRepository.save(new Paciente("Joaquin", "Tren", "12345678", "joaco@hotmail.com", "Joaco2024"));
            pacienteRepository.save(new Paciente("Valentina", "Leoz", "12345678", "valenleoz@hotmail.com", "Valen2024"));
            pacienteRepository.save(new Paciente("Mariano", "Van Staden", "23456789", "ysyagenda@gmail.com", "12345678"));


            // Guardar 5 doctores
            doctorRepository.save(new Doctor("Carlos", "Sánchez", "Neurología"));
            doctorRepository.save(new Doctor("Lucía", "Fernández", "Cardiología"));
            doctorRepository.save(new Doctor("Ana", "Rodríguez", "Pediatría"));
            doctorRepository.save(new Doctor("Fernando", "Mora", "Dermatología"));
            doctorRepository.save(new Doctor("Laura", "Suárez", "Oftalmología"));


            // Guardar 5 especialidades
            especialidadRepository.save(new Especialidad("Pediatría"));
            especialidadRepository.save(new Especialidad("Dermatología"));
            especialidadRepository.save(new Especialidad("Traumatología"));
            especialidadRepository.save(new Especialidad("Ginecología"));
            especialidadRepository.save(new Especialidad("Psiquiatría"));


            // Guardar 20 turnos (ejemplo usando fechas)
            turnoRepository.save(new Turno(pacienteRepository.findByNombre("Mariano").get(0), doctorRepository.findByEspecialidad("Neurología").get(0), LocalDateTime.of(2024, 10, 5, 10, 0)));
            turnoRepository.save(new Turno(pacienteRepository.findByNombre("Valentina").get(0), doctorRepository.findByEspecialidad("Cardiología").get(0), LocalDateTime.of(2024, 10, 6, 11, 0)));
            turnoRepository.save(new Turno(pacienteRepository.findByNombre("Rocio").get(0), doctorRepository.findByEspecialidad("Pediatría").get(0), LocalDateTime.of(2024, 10, 7, 12, 0)));
            turnoRepository.save(new Turno(pacienteRepository.findByNombre("Gonzalo").get(0), doctorRepository.findByEspecialidad("Dermatología").get(0), LocalDateTime.of(2024, 10, 8, 9, 0)));


        };
    }
}