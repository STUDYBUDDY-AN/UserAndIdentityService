package com.studybuddy.user_identity_service.controller;

import com.studybuddy.user_identity_service.dto.AuthResponse;
import com.studybuddy.user_identity_service.dto.LoginCredentialsDto;
import com.studybuddy.user_identity_service.dto.UserRegistrationDto;
import com.studybuddy.user_identity_service.entity.User;
import com.studybuddy.user_identity_service.service.Authservice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final Authservice authservice;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {

        log.info("Signup attempt for email: {}", userRegistrationDto.getEmail());
        AuthResponse authResponse = authservice.register(userRegistrationDto);
        log.info("Signup successful for email: {}", userRegistrationDto.getEmail());

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody LoginCredentialsDto loginCreds){

        log.info("Login attempt for email: {}", loginCreds.getEmail());

        return new ResponseEntity<>(authservice.login(loginCreds), HttpStatus.OK);
    }

    @PostMapping("/admin/create-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> createAdmin(@RequestBody UserRegistrationDto userRegistrationDto) {

        log.info("Admin Signup attempt for email: {}", userRegistrationDto.getEmail());
        AuthResponse authResponse = authservice.createAdmin(userRegistrationDto);
        log.info("Admin Signup successful for email: {}", userRegistrationDto.getEmail());



        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }
}
