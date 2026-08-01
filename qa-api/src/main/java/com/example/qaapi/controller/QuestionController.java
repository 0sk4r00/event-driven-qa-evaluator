package com.example.qaapi.controller;

import com.example.qaapi.dto.QuestionDTO;
import com.example.qaapi.service.QuestionService;
import com.example.qaapi.service.SseService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/questions")
@CrossOrigin(origins = "*")
public class QuestionController {

    private final QuestionService questionService;
    private final KafkaTemplate<String, QuestionDTO> kafkaTemplate;
    private final SseService sseService; //

    public QuestionController(QuestionService questionService, KafkaTemplate<String, QuestionDTO> kafkaTemplate, SseService sseService) {
        this.questionService = questionService;
        this.kafkaTemplate = kafkaTemplate;
        this.sseService = sseService;
    }

    @GetMapping("/random")
    public ResponseEntity<QuestionDTO> getRandomQuestion() {
        return ResponseEntity.ok(questionService.getRandomQuestion());
    }

    @PostMapping("/submit")
    public ResponseEntity<Void> submitAnswer(@RequestBody QuestionDTO dto) {
        kafkaTemplate.send("qa-topic", dto);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/subscribe/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable String id) {
        return sseService.createEmitter(id);
    }
}