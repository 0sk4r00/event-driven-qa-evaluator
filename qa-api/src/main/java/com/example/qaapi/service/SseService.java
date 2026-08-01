package com.example.qaapi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter createEmitter(String questionId) {
        // Ustawiamy timeout na 2 minuty (120 000 ms)
        SseEmitter emitter = new SseEmitter(120_000L);

        emitters.put(questionId, emitter);

        // Obsługa sprzątania po zakończeniu/błędzie
        emitter.onCompletion(() -> emitters.remove(questionId));
        emitter.onTimeout(() -> {
            emitter.complete();
            emitters.remove(questionId);
        });
        emitter.onError(e -> emitters.remove(questionId));

        // WAŻNE: Wysyłamy natychmiastowe zdarzenie powitalne ("INIT"),
        // aby zapobiec rzuceniu błędu 503 przez serwer/przeglądarkę.
        try {
            emitter.send(SseEmitter.event().name("INIT").data("Connected"));
        } catch (IOException e) {
            emitters.remove(questionId);
        }

        return emitter;
    }

    public void sendResult(String questionId, String feedback) {
        SseEmitter emitter = emitters.get(questionId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("evaluation-result").data(feedback));
                emitter.complete(); // Zamykamy połączenie sukcesem po dostarczeniu wyniku
            } catch (IOException e) {
                emitters.remove(questionId);
            }
        }
    }
}