package com.ysyagenda.controller;


import com.mashape.unirest.http.exceptions.UnirestException;
import com.ysyagenda.service.MailgunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ysyagenda.service.MailgunService;

@RestController
public class EmailController {

    @Autowired
    private MailgunService mailgunService;

    @GetMapping("/send-email")
    public String sendEmail() throws UnirestException {
        mailgunService.sendSimpleMessage("ysyagenda@gmail.com", "Prueba MailGun", "It's Working Mate!");
        return "Correo enviado!";
    }
}
