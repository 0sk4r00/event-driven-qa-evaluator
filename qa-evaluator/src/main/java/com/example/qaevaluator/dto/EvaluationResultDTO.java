package com.example.qaevaluator.dto;

public record EvaluationResultDTO(
        String questionId,
        String userId,
        String feedback
) {}