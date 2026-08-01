package com.example.qaapi.dto;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String token) {}
