package vivaldispring.eu.chatgptvoicedemo.components;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vivaldispring.eu.openaiapi.data.TranscriptText;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@Component
public class AudioToTextImp implements AudioToText{

    @Value("${OPENAI_API_KEY}")
    private String OPENAI_API_KEY;
    private static final String API_URL = "https://api.openai.com/v1/audio/transcriptions";

    @Override
    public String transcribeAudio(String audioFile) {
        return null;
    }

    public void transcribe() {

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(API_URL))
                    .header("Authorization", "Bearer " + OPENAI_API_KEY)
                    .header("Content-Type", "multipart/form-data")
                    .POST(HttpRequest.BodyPublishers.ofFile(Path.of("/Users/antonio/IdeaProjects/OpenAI-API/output.mp3")))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public TranscriptText ApacheTranscriber(){
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost httpPost = new HttpPost(API_URL);
            httpPost.setHeader("Authorization", "Bearer " + OPENAI_API_KEY);

            File audioFile = new File("uploads/1712075831334.webm");

            HttpEntity multiPartEntity = MultipartEntityBuilder.create()
                    .addBinaryBody("file", audioFile, ContentType.create("audio/mpeg"), audioFile.getName())
                    .addTextBody("model", "whisper-1")
                    .build();

            httpPost.setEntity(multiPartEntity);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                //System.out.println(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
                String result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                System.out.println(result);
                Gson gson = new Gson();
                response.close();
                return gson.fromJson(result, TranscriptText.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @return
     */
    public TranscriptText ApacheTranscriberInMemory(InputStream AudioFile){

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost httpPost = new HttpPost(API_URL);
            httpPost.setHeader("Authorization", "Bearer " + OPENAI_API_KEY);

            HttpEntity multiPartEntity = MultipartEntityBuilder.create()
                    .addBinaryBody("file", AudioFile, ContentType.create("audio/mpeg"), "audio.webm")
                    .addTextBody("model", "whisper-1")
                    .build();

            httpPost.setEntity(multiPartEntity);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                //System.out.println(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
                String result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                System.out.println(result);
                Gson gson = new Gson();
                response.close();
                return gson.fromJson(result, TranscriptText.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
