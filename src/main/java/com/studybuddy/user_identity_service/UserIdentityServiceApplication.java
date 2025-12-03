package com.studybuddy.user_identity_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class UserIdentityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserIdentityServiceApplication.class, args);
	}

}
