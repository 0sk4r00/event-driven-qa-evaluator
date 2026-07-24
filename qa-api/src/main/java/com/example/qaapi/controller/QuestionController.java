package com.example.qaapi.controller;

import com.example.qaapi.dto.QuestionEvaluationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private static final String TOPIC = "qa-pending-evaluations";
    private final KafkaTemplate<String, QuestionEvaluationRequest> kafkaTemplate;

    public QuestionController(KafkaTemplate<String, QuestionEvaluationRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<String> submitQuestion(@RequestBody QuestionEvaluationRequest request) {
        String id = request.id() != null ? request.id() : UUID.randomUUID().toString();
        QuestionEvaluationRequest event = new QuestionEvaluationRequest(id, request.question(), request.userAnswer());

        kafkaTemplate.send(TOPIC, id, event);

        return ResponseEntity.ok("Odpowiedź została wysłana do analizy AI. ID zdarzenia: " + id);
    }
}