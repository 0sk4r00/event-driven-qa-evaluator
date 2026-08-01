package com.example.qaapi.dto;

public record QuestionDTO(
        Long id,
        String question,
        String userAnswer
) {}