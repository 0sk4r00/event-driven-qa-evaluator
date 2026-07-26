package com.example.qaapi.dto;

public record QuestionDto(
        String id,
        String category,
        String difficulty,
        String question,
        String referenceAnswer
) {}