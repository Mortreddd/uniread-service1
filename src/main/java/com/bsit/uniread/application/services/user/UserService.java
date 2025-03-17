package com.bsit.uniread.application.services.user;

import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.repositories.user.UserRepository;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Get the users with pagination
     * @param pageNo
     * @param pageSize
     * @param query
     * @return Pagination of User or Filtered By name of User
     */
    public Page<User> getUsers(int pageNo, int pageSize, String query) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        if(!StringUtil.isNullOrEmpty(query)) {
            return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrUsernameIgnoreCase(query, query, query, pageable);
        }
        return userRepository.findAll(pageable);
    }


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

    public List<User> getUsersById(List<UUID> userIds) {
        return userRepository.findAllById(userIds);

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
     * Get the user by using username
     * @param username
     * @return user
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find " + username));
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
        return userRepository.findByUsernameContainingIgnoreCase(username)
                .isPresent();
    }



}
