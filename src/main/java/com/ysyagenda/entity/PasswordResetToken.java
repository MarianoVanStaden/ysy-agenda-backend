package com.ysyagenda.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

    @Entity
    public class PasswordResetToken {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String token;

        @OneToOne(targetEntity = Paciente.class, fetch = FetchType.EAGER)
        @JoinColumn(nullable = false, name = "paciente_id")
        private Paciente paciente;

        private LocalDateTime expiryDate;

        public PasswordResetToken() {
        }

        public PasswordResetToken(String token, Paciente paciente) {
            this.token = token;
            this.paciente = paciente;
            this.expiryDate = LocalDateTime.now().plusHours(24);
        }

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expiryDate);
        }

        // Getters and setters
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Paciente getPaciente() {
            return paciente;
        }

        public void setPaciente(Paciente paciente) {
            this.paciente = paciente;
        }

        public LocalDateTime getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(LocalDateTime expiryDate) {
            this.expiryDate = expiryDate;
        }
    }