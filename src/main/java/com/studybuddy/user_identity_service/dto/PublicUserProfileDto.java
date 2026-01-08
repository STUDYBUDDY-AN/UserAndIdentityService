package com.studybuddy.user_identity_service.dto;

import java.util.UUID;

public record PublicUserProfileDto(
        UUID id,
        String firstName,
        String lastName,
        String email
) {}
