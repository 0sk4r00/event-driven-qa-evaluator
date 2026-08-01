package com.example.qaapi.service;

import com.example.qaapi.dto.QuestionDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Service
public class QuestionLoader {

    private final ObjectMapper objectMapper;
    private List<QuestionDTO> questions = Collections.emptyList();

    public QuestionLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        try (InputStream inputStream = getClass().getResourceAsStream("/questions.json")) {
            if (inputStream != null) {
                questions = objectMapper.readValue(inputStream, new TypeReference<List<QuestionDTO>>() {});
            }
        } catch (Exception e) {
            throw new RuntimeException("Nie udało się wczytać pytań z questions.json", e);
        }
    }

    public List<QuestionDTO> getAllQuestions() {
        return questions;
    }
}