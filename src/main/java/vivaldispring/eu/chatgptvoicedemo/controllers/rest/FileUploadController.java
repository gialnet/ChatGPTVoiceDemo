package vivaldispring.eu.chatgptvoicedemo.controllers.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vivaldispring.eu.openaiapi.api_services.transcription.AudioToTextImp;
import vivaldispring.eu.openaiapi.curl.services.SseService;
import vivaldispring.eu.openaiapi.data.TranscriptText;

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

    @PostMapping(value = "/upload")
    public ResponseEntity<?> uploadAudio(@RequestParam("file") MultipartFile file) {


        if (file.isEmpty()) {
            log.info("Server info '{}'", "fichero vacio");
            return ResponseEntity.badRequest().body("No file uploaded.");
        }
        /*
        if (!file.getContentType().equals("audio/ogg; codecs=opus") && !file.getContentType().equals("audio/x-wav")) {
            log.info("Server info '{}'", "formato de fichero no valido");
            return ResponseEntity.badRequest().body("File format is not WAV.");
        }
        else log.info("Server info '{}'", "formato valido");
*/
        try {
            Files.createDirectories(rootLocation); // Ensure the directory exists

            // Generate a unique file name to prevent overwriting
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String savedFileName = System.currentTimeMillis() + fileExtension; // Could also use UUID.randomUUID()
            Path destinationFile = rootLocation.resolve(Paths.get(savedFileName)).normalize().toAbsolutePath();

            // Ensure the file is not being saved outside the intended directory
            if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot store file outside current directory.");
            }


            file.transferTo(destinationFile);

            //Files.copy(file. getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok().body("File uploaded successfully: " + destinationFile.toString());

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the file due to an error: " + e.getMessage());
        }
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
