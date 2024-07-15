package vivaldispring.eu.chatgptvoicedemo.services.text_to_speech;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class TextToSpeechImp implements TextToSpeech {

    private final String star = "{\"model\":\"tts-1\",\"input\":\"";

    // voices male Onyx female Shimmer
    private final String second = "\",\"voice\":\"";

    private final String end = "\"}";


    @Value("${myopenai.apikey}")
    private String OPENAI_API_KEY;


    @Override
    public boolean play(String text, String voice, String fileName) throws Exception {

        fileName.concat(".mp3");
        Path path = Paths.get(fileName);

        String bodyString = star.concat(text).concat(second).concat(voice).concat(end);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.openai.com/v1/audio/speech"))
                .header("Authorization", "Bearer " + OPENAI_API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(bodyString))
                .build();

        HttpResponse<Path> response = client.send(request,
                HttpResponse.BodyHandlers.ofFile(Paths.get("output.mp3"),
                        StandardOpenOption.CREATE, StandardOpenOption.WRITE));

        if(response.statusCode() == 200) {
            System.out.println("Audio was saved to output.mp3");
        } else {
            System.out.println("Failed to retrieve audio: " + response.statusCode());
        }
        return true;
    }
}
