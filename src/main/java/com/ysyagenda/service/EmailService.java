package com.ysyagenda.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender; // Inyectar el JavaMailSender

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("tu_correo@gmail.com"); // Cambia esto por el email que uses para enviar
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message); // Asegúrate de que mailSender esté inyectado
    }
}
