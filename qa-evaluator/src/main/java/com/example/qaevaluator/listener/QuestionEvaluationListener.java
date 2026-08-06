package com.example.qaevaluator.listener;

import com.example.qaevaluator.dto.QuestionDTO;
import com.example.qaevaluator.dto.EvaluationResultDTO;
import com.example.qaevaluator.service.LlamaEvaluationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuestionEvaluationListener {

    private final LlamaEvaluationService llamaEvaluationService;
    private final KafkaTemplate<String, EvaluationResultDTO> kafkaTemplate;

    public QuestionEvaluationListener(LlamaEvaluationService llamaEvaluationService,
                                      KafkaTemplate<String, EvaluationResultDTO> kafkaTemplate) {
        this.llamaEvaluationService = llamaEvaluationService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "qa-topic", groupId = "evaluator-group")
    public void consume(QuestionDTO dto) {
        log.info("Odebrano pytanie do oceny dla userId: {}", dto.userId());

        // 1. Ocenia odpowiedź w Llamie
        String feedback = llamaEvaluationService.evaluateAnswer(dto);

        // 2. Publikuje wynik na topik 'qa-results' (skąd qa-api go odbierze i wyśle przez SSE)
        EvaluationResultDTO result = new EvaluationResultDTO(dto.id(), dto.userId(), feedback);
        kafkaTemplate.send("qa-results", result);

        log.info("Wysłano ocenę na topik 'qa-results' dla userId: {}", dto.userId());
    }
}