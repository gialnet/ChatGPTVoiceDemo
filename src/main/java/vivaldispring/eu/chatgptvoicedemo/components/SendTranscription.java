package vivaldispring.eu.chatgptvoicedemo.components;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Component
public class SendTranscription {

    public boolean SendMessage() throws URISyntaxException, IOException, InterruptedException {

        //String bodyString = star.concat(text).concat(end);

        HttpClient client = HttpClient.newHttpClient();

        // Datos JSON de ejemplo
        String jsonPayload = "{\"name\": \"John Doe\", \"age\": 30}";


        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/notifications"))
                .header("Cache-Control", "no-cache")
                .header("Content-Type", "text/event-stream")
                //.header("Connection", "keep-alive")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                .build();

        // Enviar la solicitud y obtener la respuesta
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 200) {
            System.out.println("mensaje enviado");
        } else {
            System.out.println("Fallo el envio");
        }
        return true;
    }
}
