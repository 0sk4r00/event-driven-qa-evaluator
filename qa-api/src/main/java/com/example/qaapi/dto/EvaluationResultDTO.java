package com.example.qaapi.dto;

public class EvaluationResultDTO {
    private String id;
    private String feedback;

    // Bezargumentowy konstruktor jest wymagany przez Jacksona do deserializacji JSON
    public EvaluationResultDTO() {}

    public EvaluationResultDTO(String id, String feedback) {
        this.id = id;
        this.feedback = feedback;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}