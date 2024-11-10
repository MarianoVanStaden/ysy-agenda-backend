/*package com.ysyagenda.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MailgunService {

    @Value("${mailgun.api-key}")
    private String apiKey;

    @Value("${mailgun.domain}")
    private String domain;

    public void sendPasswordResetEmail(String toEmail, String subject, String body) throws Exception {
        String apiUrl = "https://api.mailgun.net/v3/" + domain + "/messages";

        try {

            // Realizando la solicitud HTTP POST a Mailgun
            HttpResponse<String> response = Unirest.post(apiUrl)
                    .basicAuth("api", apiKey)  // Usar basicAuth con la clave API
                    .field("from", "noreply@" + domain)
                    .field("to", toEmail)
                    .field("subject", subject)
                    .field("text", body)
                    .asString();

            // Verificar el código de estado HTTP
            if (response.getStatus() != 200) {
                // Si el estado HTTP no es 200, lanzar una excepción con el mensaje de error
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al enviar el correo de recuperación. Respuesta de Mailgun: " + response.getBody());
            }

            // Mostrar la respuesta de Mailgun para fines de depuración
            System.out.println("Mailgun Response: " + response.getBody());

        } catch (Exception e) {
            // Manejar excepciones y registrar el error
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al procesar la solicitud de recuperación de contraseña", e);
        }
    }
}
*/