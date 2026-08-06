package com.example.qaevaluator.dto;

public record QuestionDTO(
        String id,
        String userId,
        String question,
        String userAnswer

) {}