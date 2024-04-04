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
import vivaldispring.eu.chatgptvoicedemo.data.TranscriptText;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class AudioToTextImp implements AudioToText{

    @Value("${myopenai.apikey}")
    private String OPENAI_API_KEY;
    private static final String API_URL = "https://api.openai.com/v1/audio/transcriptions";


    /**
     * Read an audio file from memory
     * @return object with the audio transcription
     */
    @Override
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
