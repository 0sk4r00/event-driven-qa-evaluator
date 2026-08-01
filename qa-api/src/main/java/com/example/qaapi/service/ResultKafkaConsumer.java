package com.example.qaapi.service;

import com.example.qaapi.dto.EvaluationResultDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ResultKafkaConsumer {

    private final SseService sseService;

    public ResultKafkaConsumer(SseService sseService) {
        this.sseService = sseService;
    }

    @KafkaListener(topics = "qa-results", groupId = "api-group")
    public void consumeResult(EvaluationResultDTO result) {
        // Natychmiast przekazujemy wynik z Kafki do aktywnego strumienia SSE
        sseService.sendResult(result.getId(), result.getFeedback());
    }
}