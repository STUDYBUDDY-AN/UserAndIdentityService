package com.studybuddy.user_identity_service.service.Implementation;

import com.studybuddy.user_identity_service.dto.AuthResponse;
import com.studybuddy.user_identity_service.dto.LoginCredentialsDto;
import com.studybuddy.user_identity_service.dto.UserRegistrationDto;
import com.studybuddy.user_identity_service.entity.Enums.Role;
import com.studybuddy.user_identity_service.entity.User;
import com.studybuddy.user_identity_service.exception.EmailAlreadyExistsException;
import com.studybuddy.user_identity_service.exception.ResourceNotFoundException;
import com.studybuddy.user_identity_service.repository.UserRepository;
import com.studybuddy.user_identity_service.service.Authservice;
import com.studybuddy.user_identity_service.service.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements Authservice {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JWTService jwtService;

    private final AuthenticationManager authenticationManager;



    @Override
    public AuthResponse register(UserRegistrationDto request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(Role.STUDENT)) //  DEFAULT ROLE
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthResponse(token, user.getEmail());
    }


    @Override
    public AuthResponse login(LoginCredentialsDto request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new ResourceNotFoundException("USER NOT FOUND"));

        String jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthResponse createAdmin(UserRegistrationDto request) {
        User admin = User.builder()
                .id(UUID.randomUUID())
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(Role.ADMIN))
                .build();

        userRepository.save(admin);

        String token = jwtService.generateToken(admin);

        return new AuthResponse(token, admin.getEmail());
    }
}
