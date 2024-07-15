package vivaldispring.eu.chatgptvoicedemo.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vivaldispring.eu.chatgptvoicedemo.services.text_to_speech.TextToSpeechImp;

@SpringBootTest
public class TextToSpeechTest {

    String text = "Curso de introducción a OpenAI API con asistentes";

    private final String star = "{\"model\":\"tts-1\",\"input\":\"";
    private final String end = "\",\"voice\":\"alloy\"}";

    @Autowired
    private TextToSpeechImp textToSpeechImp;

    @Test
    void playOnyxTest() throws Exception {
        textToSpeechImp.play(text,"onyx", "VoiceOnyxMP3");
    }

    @Test
    void playShimmerTest() throws Exception {
        textToSpeechImp.play(text,"shimmer", "VoiceShimmerMP3");
    }

    @Test
    void concatTest(){

        String bodyString = star.concat(text).concat(end);

        System.out.println(bodyString);
    }
}
