package com.example.qaapi.service;

import com.example.qaapi.dto.QuestionDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@Service
public class QuestionService {

    private final ObjectMapper objectMapper;
    private final Random random = new Random();
    private List<QuestionDTO> questions;

    public QuestionService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() throws IOException {
        ClassPathResource resource = new ClassPathResource("questions.json");
        this.questions = objectMapper.readValue(
                resource.getInputStream(),
                new TypeReference<List<QuestionDTO>>() {}
        );
    }

    public QuestionDTO getRandomQuestion() {
        if (questions == null || questions.isEmpty()) {
            throw new RuntimeException("Brak pytań w pliku JSON!");
        }
        return questions.get(random.nextInt(questions.size()));
    }
}