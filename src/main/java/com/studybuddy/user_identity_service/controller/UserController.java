package com.studybuddy.user_identity_service.controller;

import com.studybuddy.user_identity_service.dto.PublicUserProfileDto;
import com.studybuddy.user_identity_service.entity.User;
import com.studybuddy.user_identity_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<PublicUserProfileDto> getUserProfile(@PathVariable UUID id) {

        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found with id: "+ id));

        PublicUserProfileDto response = new PublicUserProfileDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );

        return ResponseEntity.ok(response);
    }
}