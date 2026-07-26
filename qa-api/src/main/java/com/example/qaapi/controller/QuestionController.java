package com.example.qaapi.controller;

import com.example.qaapi.dto.QuestionEvaluationRequest;
import com.example.qaapi.service.QuestionLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import com.example.qaapi.dto.QuestionDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private static final String TOPIC = "qa-pending-evaluations";
    private final KafkaTemplate<String, QuestionEvaluationRequest> kafkaTemplate;
    private final QuestionLoader questionLoader;

    public QuestionController(KafkaTemplate<String, QuestionEvaluationRequest> kafkaTemplate, QuestionLoader questionLoader) {
        this.kafkaTemplate = kafkaTemplate;
        this.questionLoader = questionLoader;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<String> submitQuestion(@RequestBody QuestionEvaluationRequest request) {
        String id = request.id() != null ? request.id() : UUID.randomUUID().toString();

        QuestionEvaluationRequest event = new QuestionEvaluationRequest(
                id,
                request.question(),
                request.referenceAnswer(),
                request.userAnswer()
        );

        kafkaTemplate.send(TOPIC, id, event);

        return ResponseEntity.ok("Odpowiedź została wysłana do analizy AI. ID zdarzenia: " + id);
    }

    @GetMapping
    public ResponseEntity<List<QuestionDto>> getAllQuestions() {
        return ResponseEntity.ok(questionLoader.getAllQuestions());
    }
    @GetMapping("/{id}")
    public ResponseEntity<QuestionDto> getQuestionById(@PathVariable String id) {
        return questionLoader.getAllQuestions().stream()
                .filter(q -> q.id().equalsIgnoreCase(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}