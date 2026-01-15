package com.studybuddy.user_identity_service.dto;

import lombok.Builder;

@Builder
public record AuthResponse(String token, String email) {

}

