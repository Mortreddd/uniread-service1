package com.bsit.uniread.application.services.user;

import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.repositories.user.UserRepository;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Get the user based on provided id
     * @param userId
     * @return User
     * @throws ResourceNotFoundException
     */
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find user"));
    }

    /**
     * Get the user based on email
     * @params email
     * @return User
     * @throws ResourceNotFoundException
     */
    public User getUserByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find user"));
    }

    /**
     * Accepts a User as a parameter to save in a repository
     * @param user
     * @return User
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User verifyUserEmail(User user) {
        user.setEmailVerifiedAt(DateUtil.now());
        user.setUpdatedAt(DateUtil.now());
        return saveUser(user);
    }

    /**
     * Check the email if already exists
     * @param email
     * @return Boolean
     */
    public Boolean emailExists(String email) {
         return userRepository.findByEmail(email)
                 .isPresent();
    }

    /**
     * Check if the email does not exist
     * @param email
     * @return Boolean
     */
    public Boolean emailNotExists(String email) {
        return !emailExists(email);
    }

    /**
     * Check the username if already taken
     * @param username
     * @return Boolean
     */
    public Boolean usernameExists(String username) {
        return userRepository.findByUsername(username)
                .isPresent();
    }



}
