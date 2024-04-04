package vivaldispring.eu.chatgptvoicedemo.controllers.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import vivaldispring.eu.chatgptvoicedemo.services.SseService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalTime;

@RestController
public class SendMessageController {

    private final SendTranscription sendTranscription;
    private final SseService sseService;

    public SendMessageController(SendTranscription sendTranscription, vivaldispring.eu.openaiapi.curl.services.SseService sseService) {
        this.sendTranscription = sendTranscription;
        this.sseService = sseService;
    }

    @GetMapping(path = "/push/{message}")
    private void envia(@PathVariable String message) throws URISyntaxException, IOException, InterruptedException {
        sseService.sendEvents(message);
    }

    /**
     * This work
     * @return
     */
    @GetMapping(path = "/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamFlux() {

        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> "Flux - " + LocalTime.now().toString());
    }

    /**
     *
     * @return
     */
    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        // Add the emitter to a list of subscribers or handle it in another way
        sseService.addEmitter(emitter);
        return emitter;
    }
}
