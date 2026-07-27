package com.example.qaevaluator.service;

import com.example.qaevaluator.dto.QuestionDTO;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LlamaEvaluationService {

    private final ChatModel chatModel;

    public LlamaEvaluationService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String evaluateAnswer(QuestionDTO request) {
        var systemMessage = new SystemMessage("""
            Jesteś obiektywnym egzaminatorem. Twoim zadaniem jest ocena odpowiedzi użytkownika na podstawie podanej odpowiedzi wzorcowej (referenceAnswer).
            Oceń odpowiedź w skali 0-10, podaj krótkie uzasadnienie oraz ewentualne punkty do poprawy.
            """);

        var userMessage = new UserMessage(String.format("""
            Pytanie: %s
            Odpowiedź użytkownika: %s
            """, request.question(), request.userAnswer()));

        // Spring AI sam buduje JSON, wysyła go do Groq i odbiera odpowiedź!
        var prompt = new Prompt(List.of(systemMessage, userMessage));

        return chatModel.call(prompt).getResult().getOutput().getText();
    }
}