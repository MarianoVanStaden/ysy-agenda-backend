// Updated MailgunService.java
package com.ysyagenda.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailgunService {
    @Value("${mailgun.api-key}")
    private String apiKey;

    @Value("${mailgun.domain}")
    private String domain;

    public void sendPasswordResetEmail(String toEmail, String resetLink) throws Exception {
        String apiUrl = "https://api.mailgun.net/v3/" + domain + "/messages";
        String subject = "Recuperación de contraseña";
        String body = String.format(
                "Para restablecer tu contraseña, haz clic en el siguiente enlace:\n\n%s\n\n" +
                        "Este enlace expirará en 24 horas.\n\n" +
                        "Si no solicitaste este cambio, ignora este correo.",
                resetLink
        );

        HttpResponse<JsonNode> response = Unirest.post(apiUrl)
                .basicAuth("api", apiKey)
                .queryString("from", "no-reply@" + domain)
                .queryString("to", toEmail)
                .queryString("subject", subject)
                .queryString("text", body)
                .asJson();

        if (response.getStatus() != 200) {
            throw new RuntimeException("Error al enviar el correo");
        }
    }
}
