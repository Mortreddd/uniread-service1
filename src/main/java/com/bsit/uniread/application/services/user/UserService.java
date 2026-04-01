package com.bsit.uniread.application.services.user;

import com.bsit.uniread.application.dto.request.user.UserFilter;
import com.bsit.uniread.application.dto.response.auth.GoogleUserInfoResponse;
import com.bsit.uniread.application.dto.response.user.CurrentUser;
import com.bsit.uniread.application.dto.response.user.UserDto;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import com.bsit.uniread.domain.entities.user.Role;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.domain.mappers.user.UserMapper;
import com.bsit.uniread.infrastructure.handler.exceptions.DuplicateResourceException;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.repositories.user.UserRepository;
import com.bsit.uniread.infrastructure.specifications.user.UserSpecification;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private static final int DEFAULT_PASSWORD_LENGTH = 16;

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserDto> searchUsers(CustomUserDetails userDetails, UserFilter filter) {
        UUID authUserId = userDetails != null ? userDetails.getId() : null;

        Specification<User> spec = Specification.where(UserSpecification.hasQuery(filter.getQuery()))
                .and(UserSpecification.hasAuthUser(authUserId))
                .and(UserSpecification.hasBanned(filter.getBannedAt()))
                .and(UserSpecification.hasDeleted(filter.getDeletedAt()))
                .and(UserSpecification.hasEmailVerified(filter.getEmailVerified()));

        Sort.Direction direction = "desc".equalsIgnoreCase(filter.getSortBy())
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(
                filter.getPageNo(),
                filter.getPageSize(),
                Sort.by(direction, filter.getOrderBy())
        );

        return userRepository.findAll(spec, pageable)
                .map(userMapper::toDto);
    }

    public UserDto getUserById(UUID userId) {
        User user = findUserById(userId);
        return userMapper.toDto(user);
    }

    public CurrentUser getCurrentUser(UUID currentUserId) {
        return userRepository.findCurrentUserById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + currentUserId));
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isUsernameExists(String username) {
        return userRepository.existsByUsernameContainingIgnoreCase(username);
    }

    @Transactional
    public User createGoogleUser(GoogleUserInfoResponse userInfo) {
        validateGoogleUserInfo(userInfo);

        User user = User.builder()
                .email(userInfo.getEmail())
                .username(generateTemporaryUsername(userInfo.getEmail()))
                .password(passwordEncoder.encode(generateRandomPassword()))
                .emailVerifiedAt(DateUtil.now())
                .googleUuid(userInfo.getSub())
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        log.info("Created new Google user: {} with email: {}", savedUser.getId(), savedUser.getEmail());

        return savedUser;
    }

    @Transactional
    public void updateUsername(UUID userId, String username) {
        if (isUsernameExists(username)) {
            throw new DuplicateResourceException("Username '" + username + "' is already taken");
        }

        User user = findUserById(userId);
        user.setUsername(username);
        user.setUpdatedAt(DateUtil.now());
        userRepository.save(user);

        log.info("Updated username for user {} to: {}", userId, username);
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    private void validateGoogleUserInfo(GoogleUserInfoResponse userInfo) {
        if (userInfo == null) {
            throw new IllegalArgumentException("Google user information cannot be null");
        }
        if (userInfo.getEmail() == null || userInfo.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required for Google user registration");
        }
        if (userInfo.getSub() == null || userInfo.getSub().isBlank()) {
            throw new IllegalArgumentException("Google user ID (sub) is required");
        }
        if (isEmailExists(userInfo.getEmail())) {
            throw new DuplicateResourceException("User with email " + userInfo.getEmail() + " already exists");
        }
    }

    private String generateTemporaryUsername(String email) {
        String baseUsername = email.split("@")[0];
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        return baseUsername + "_" + uniqueSuffix;
    }

    private String generateRandomPassword() {
        return StringUtils.randomAlphanumeric(DEFAULT_PASSWORD_LENGTH);
    }
}