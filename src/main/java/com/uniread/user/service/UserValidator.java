package com.uniread.user.service;

import com.uniread.user.repositories.UserRepository;
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
