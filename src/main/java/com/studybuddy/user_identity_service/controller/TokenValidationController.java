package com.studybuddy.user_identity_service.controller;

import com.studybuddy.user_identity_service.dto.TokenVerificationRequest;
import com.studybuddy.user_identity_service.dto.TokenVerificationResponse;
import com.studybuddy.user_identity_service.service.Implementation.UserDetailsServiceImpl;
import com.studybuddy.user_identity_service.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class TokenValidationController {

    private final JWTService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/verify")
    public ResponseEntity<TokenVerificationResponse> verifyToken(@RequestBody TokenVerificationRequest request) {
        String token = request.getToken();
        try {
            // 1. Extract ID (Subject)
            String userId = jwtService.extractUserName(token);
            
            // 2. Load User from DB
            UserDetails userDetails = userDetailsService.loadUserById(userId);

            // 3. Validate Token against User
            if (jwtService.isTokenValid(token, userDetails)) {
                return ResponseEntity.ok(TokenVerificationResponse.builder()
                        .isValid(true)
                        .userId(userId)
                        .email(userDetails.getUsername()) // In your User entity, getUsername() returns email
                        .expirationDate(jwtService.extractExpirationDate(token))
                        .message("Token is valid")
                        .build());
            }
        } catch (Exception e) {
            // Token is expired, malformed, or user does not exist
            return ResponseEntity.ok(TokenVerificationResponse.builder()
                    .isValid(false)
                    .message("Token validation failed: " + e.getMessage())
                    .build());
        }

        return ResponseEntity.ok(TokenVerificationResponse.builder()
                .isValid(false)
                .message("Token is invalid")
                .build());
    }
}