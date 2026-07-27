package com.example.qaevaluator.listener; // lub package ...service

import com.example.qaevaluator.dto.QuestionDTO;
import com.example.qaevaluator.dto.EvaluationResultDTO;
import com.example.qaevaluator.service.LlamaEvaluationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerListener {

    private final LlamaEvaluationService llamaEvaluationService; // <-- ZMIANA (był GroqService)
    private final KafkaTemplate<String, EvaluationResultDTO> kafkaTemplate;

    public KafkaConsumerListener(LlamaEvaluationService llamaEvaluationService,
                                 KafkaTemplate<String, EvaluationResultDTO> kafkaTemplate) {
        this.llamaEvaluationService = llamaEvaluationService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "qa-topic", groupId = "evaluator-group")
    public void consume(QuestionDTO dto) {
        // Generujemy ocenę przy użyciu Spring AI i Llama 3.3
        String feedback = llamaEvaluationService.evaluateAnswer(dto);

        // Odpowiedź trafia z powrotem do Redpandy
        EvaluationResultDTO result = new EvaluationResultDTO(dto.id(), feedback);
        kafkaTemplate.send("qa-results", result);
    }
}