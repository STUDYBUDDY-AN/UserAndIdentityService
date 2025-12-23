package com.studybuddy.user_identity_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public String studentAccess() {
        log.info("STUDENT endpoint accessed");
        return "Hello STUDENT 👋 You are authorized";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        log.info("ADMIN endpoint accessed");
        return "Hello ADMIN 👋 You are authorized";
    }

    @GetMapping("/both")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public String bothAccess() {
        log.info("STUDENT or ADMIN endpoint accessed");
        return "Hello 👋 STUDENT or ADMIN can access this";
    }
}
