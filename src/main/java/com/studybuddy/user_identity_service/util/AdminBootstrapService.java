package com.studybuddy.user_identity_service.util;

import com.studybuddy.user_identity_service.entity.Enums.Role;
import com.studybuddy.user_identity_service.entity.User;
import com.studybuddy.user_identity_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdminBootstrapService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void createAdminIfNotExists() {

        String email = "admin@studybuddy.com";

        if (!userRepository.existsByEmail(email)) {

            User admin = User.builder()
                    .id(UUID.randomUUID())
                    .firstName("System")
                    .lastName("Admin")
                    .email(email)
                    .password(passwordEncoder.encode("admin123"))
                    .roles(List.of(Role.ADMIN))
                    .build();

            userRepository.save(admin);
        }
    }
}