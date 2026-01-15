package com.studybuddy.user_identity_service.service;

import com.studybuddy.user_identity_service.entity.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

public interface JWTService {
    String generateToken(User user);
    boolean isTokenValid(String token, UserDetails userDetails);
    boolean isTokenExpired(String token);
    String extractUserName(String token);
    Date extractExpirationDate(String token);
    <T> T extractClaims(String token, Function<Claims, T> claimsResolver);
    Claims extractAllClaims(String token);
    Key getSignInKey();
}
