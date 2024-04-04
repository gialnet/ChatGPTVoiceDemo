package vivaldispring.eu.chatgptvoicedemo.controllers.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import vivaldispring.eu.chatgptvoicedemo.services.SseService;

@RestController
public class SendServerMessageController {

    private final SseService sseService;

    public SendServerMessageController(SseService sseService) {
        this.sseService = sseService;
    }


    /**
     * The emitter object
     * @return emitter
     */
    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        // Add the emitter to a list of subscribers or handle it in another way
        sseService.addEmitter(emitter);
        return emitter;
    }
}
