package com.example.qaevaluator.dto;

public record QuestionEvaluationRequest(
        String id,
        String question,
        String referenceAnswer,
        String userAnswer
) {}