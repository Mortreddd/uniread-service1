package com.bsit.uniread.application.services.user;

import com.bsit.uniread.domain.entities.user.UserProfile;
import com.bsit.uniread.infrastructure.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;


    public Boolean emailExists(String email) {
        if(email == null || email.isBlank()) return false;

        return userRepository.existsByEmail(email);
    }

    public Boolean usernameExist(String username) {
        if(username == null || username.isBlank()) return false;
        return userRepository.existsByUsername(username);
    }
}
