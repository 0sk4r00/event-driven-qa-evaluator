package com.example.qaapi.dto;

public record QuestionEvaluationRequest(
        String id,
        String question,
        String userAnswer
) {}