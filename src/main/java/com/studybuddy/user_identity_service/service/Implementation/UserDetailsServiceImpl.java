package com.studybuddy.user_identity_service.service.Implementation;


import com.studybuddy.user_identity_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    UserRepository userRepository;

    // Used by AuthenticationManager for LOGIN (Email)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: "+ email));
    }

    // Used by JWTFilter for TOKEN VALIDATION (ID)
    public UserDetails loadUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with id: "+ userId));
    }
}