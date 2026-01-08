package com.studybuddy.user_identity_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenVerificationResponse {
    private boolean isValid;
    private String userId;
    private String email;
    private Date expirationDate;
    private String message;
}