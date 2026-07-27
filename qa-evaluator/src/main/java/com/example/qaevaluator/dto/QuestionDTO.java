package com.example.qaevaluator.dto;

public record QuestionDTO(
        String id,
        String question,
        String userAnswer
) {}