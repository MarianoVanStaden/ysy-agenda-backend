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

           
        };
    }
}