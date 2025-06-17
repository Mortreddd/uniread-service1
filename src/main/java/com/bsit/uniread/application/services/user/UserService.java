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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getUserByEmailOrThrow(userDetails.getUsername());
    }

    /**
     * Get the users with pagination
     * @param pageNo
     * @param pageSize
     * @param query
     * @return Pagination of User or Filtered By name of User
     */
    @Transactional(readOnly = true)
    public Page<User> getUsers(int pageNo, int pageSize, String query) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        if(!StringUtil.isNullOrEmpty(query)) {
            return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrUsernameIgnoreCase(query, query, query, pageable);
        }
        return userRepository.findAll(pageable);
    }


    /**
     * Get the user based on googleUuid
     * @param googleUuid
     * @return user or null
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByGoogleUuid(String googleUuid) {
        return userRepository.findByGoogleUuid(googleUuid);
    }

    /**
     * Get the user based on provided id
     * @param userId
     * @return User
     * @throws ResourceNotFoundException
     */
    @Transactional(readOnly = true)
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find user"));
    }

    /**
     * Get the users based on list of ids
     * @param userIds
     * @return list of users
     */
    @Transactional(readOnly = true)
    public List<User> getUsersById(List<UUID> userIds) {
        return userRepository.findAllById(userIds);

    }


    @Transactional
    public User saveIfExistsByEmail(User user) {
        return userRepository.findByEmail(user.getEmail())
                .orElse(userRepository.save(user));
    }
    /**
     * Get the user based on email
     * @params email
     * @return User
     * @throws ResourceNotFoundException
     */
    @Transactional(readOnly = true)
    public User getUserByEmailOrThrow(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find user"));
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Get the user by using username
     * @param username
     * @return user
     */
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find " + username));
    }

    /**
     * Accepts a User as a parameter to save in a repository
     * @param user
     * @return User
     */
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Verify the user's email
     * @param user
     * @return User
     */
    public User verifyUserEmail(User user) {
        user.setEmailVerifiedAt(DateUtil.now());
        user.setUpdatedAt(DateUtil.now());
        return save(user);
    }
    public User updateUsername(UUID userId, String username) {
        User user = getUserById(userId);
        user.setUsername(username);
        return save(user);

    }
    /**
     * Check the email if already exists
     * @param email
     * @return Boolean
     */
    @Transactional(readOnly = true)
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
