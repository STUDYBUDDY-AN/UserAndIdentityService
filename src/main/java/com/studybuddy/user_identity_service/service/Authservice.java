package com.studybuddy.user_identity_service.service;


import com.studybuddy.user_identity_service.dto.AuthResponse;
import com.studybuddy.user_identity_service.dto.LoginCredentialsDto;
import com.studybuddy.user_identity_service.dto.UserRegistrationDto;
import com.studybuddy.user_identity_service.entity.User;

public interface Authservice {

    AuthResponse register(UserRegistrationDto request);

    AuthResponse login(LoginCredentialsDto request);

    AuthResponse createAdmin(UserRegistrationDto request);
}
