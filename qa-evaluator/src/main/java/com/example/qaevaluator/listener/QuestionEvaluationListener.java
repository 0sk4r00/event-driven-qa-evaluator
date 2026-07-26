package com.example.qaevaluator.listener;

import com.example.qaevaluator.dto.QuestionEvaluationRequest;
import com.example.qaevaluator.service.LlamaEvaluationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class QuestionEvaluationListener {

    private static final Logger log = LoggerFactory.getLogger(QuestionEvaluationListener.class);
    private final LlamaEvaluationService evaluationService;

    public QuestionEvaluationListener(LlamaEvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    // TUTAJ dajemy adnotację Kafki
    @KafkaListener(topics = "qa-pending-evaluations", groupId = "qa-evaluator-group")
    public void consume(QuestionEvaluationRequest request) {
        log.info("Odebrano zdarzenie do oceny dla ID: {}", request.id());

        // Wywołujemy Twój LlamaEvaluationService
        String evaluationResult = evaluationService.evaluateAnswer(request);

        log.info("=== Wynik z Llamy dla ID: {} ===", request.id());
        log.info(evaluationResult);
    }
}