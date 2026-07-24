package com.example.qaevaluator.listener;

import com.example.qaevaluator.dto.QuestionEvaluationRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EvaluationListener {

    private final ChatClient chatClient;

    public EvaluationListener(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @KafkaListener(topics = "qa-pending-evaluations", groupId = "qa-evaluator-group")
    public void processEvaluation(QuestionEvaluationRequest request) {
        System.out.println("==========================================");
        System.out.println("📥 ODEBRANO ODPOWIEDŹ UŻYTKOWNIKA DO OCENY");
        System.out.println("ID: " + request.id());
        System.out.println("Pytanie: " + request.question());
        System.out.println("Odpowiedź użytkownika: " + request.userAnswer());

        String prompt = """
            You are an expert technical evaluator and mentor. 
            Evaluate the user's answer to the following question.
            
            QUESTION: %s
            USER'S ANSWER: %s
            
            Provide your response strictly in the following structure:
            VERDICT: [CORRECT / PARTIALLY CORRECT / INCORRECT]
            SCORE: [0 to 10]
            EXPLANATION: [Brief, constructive feedback focusing on technical accuracy]
            MISSING KEYWORDS: [Key concepts or terminology omitted by the user, if any]
            """.formatted(request.question(), request.userAnswer());

        System.out.println("🤖 Analizowanie odpowiedzi przez Groq (Llama 3.1)...");

        String aiResult = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        System.out.println("✅ WERDYKT AI:");
        System.out.println(aiResult);
        System.out.println("==========================================");
    }
}