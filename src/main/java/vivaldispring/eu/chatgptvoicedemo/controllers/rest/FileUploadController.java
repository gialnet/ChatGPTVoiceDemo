package vivaldispring.eu.chatgptvoicedemo.controllers.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vivaldispring.eu.chatgptvoicedemo.components.AudioToTextImp;
import vivaldispring.eu.chatgptvoicedemo.data.TranscriptText;
import vivaldispring.eu.chatgptvoicedemo.services.SseService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("/api")
public class FileUploadController {

    private final Path rootLocation = Paths.get("/home/antonio/IdeaProjects/OpenAI-API-vivaldi/uploads/");
    private final AudioToTextImp audioToTextImp;
    private final SseService sseService;

    public FileUploadController(AudioToTextImp audioToTextImp, SseService sseService) {
        this.audioToTextImp = audioToTextImp;
        this.sseService = sseService;
    }

    @PostMapping(value = "/upload2")
    public ResponseEntity<?> uploadAudioStream(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            log.info("Server info '{}'", "fichero vacio");
            return ResponseEntity.badRequest().body("No file uploaded.");
        }

        try {

            InputStream inputStream = file.getInputStream();

            // send to transcript
            TranscriptText transcriptText = audioToTextImp.ApacheTranscriberInMemory(inputStream);
            sseService.sendEvents(transcriptText.getText());

            return ResponseEntity.ok().body("File uploaded successfully");

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the file due to an error: " + e.getMessage());
        }
    }
}
