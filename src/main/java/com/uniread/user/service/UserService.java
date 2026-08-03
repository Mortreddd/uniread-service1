package com.uniread.user.service;

import com.uniread.auth.dto.request.UserRegistrationRequest;
import com.uniread.common.dto.api.SuccessResponse;
import com.uniread.common.exceptions.ValidationException;
import com.uniread.user.domain.entities.Role;
import com.uniread.user.domain.events.UpdateEmailEvent;
import com.uniread.user.dto.request.UserFilter;
import com.uniread.auth.dto.response.GoogleUserInfoResponse;
import com.uniread.user.dto.response.CurrentUser;
import com.uniread.user.dto.response.UserDto;
import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.user.domain.entities.User;
import com.uniread.user.dto.response.UserSearchDto;
import com.uniread.user.mappers.UserMapper;
import com.uniread.common.exceptions.DuplicateResourceException;
import com.uniread.common.exceptions.ResourceNotFoundException;
import com.uniread.user.repositories.UserRepository;
import com.uniread.user.specifications.UserSpecification;
import com.uniread.common.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.time.Instant;
import java.util.List;
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
    private final ApplicationEventPublisher publisher;

    @Transactional(readOnly = true)
    public Page<UserDto> getUsers(CustomUserDetails userDetails, UserFilter filter) {
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

    public Page<UserSearchDto> searchUsers(CustomUserDetails userDetails, UserFilter filter) {
        Sort.Direction direction = "desc".equalsIgnoreCase(filter.getSortBy())
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(
                filter.getPageNo(),
                filter.getPageSize(),
                Sort.by(direction, filter.getOrderBy())
        );
        return userRepository.findPublicProfiles(userDetails.getId(), filter.getQuery(), pageable)
                .map(userMapper::toSearchDto);
    }

    public UserDto getUserById(UUID userId) {
        User user = findUserById(userId);
        return userMapper.toDto(user);
    }

    public CurrentUser getCurrentUser(UUID currentUserId) {
        var user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + currentUserId));

        return buildCurrentUser(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public SuccessResponse updateUsername(UUID userId, String username) {
        if (isUsernameExists(username)) {
            throw new DuplicateResourceException("Username " + username + " is already taken");
        }



        userRepository.updateUsername(username, userId);
        log.info("Updated username for user {} to: {}", userId, username);

        return SuccessResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully updated username")
                .build();
    }

    @Transactional
    public SuccessResponse updateEmail(UUID userId, String email, String username) {
        if (isEmailExists(email)) {
            throw new DuplicateResourceException("Email " + email + " is already taken");
        }

        userRepository.updateEmail(userId, email);
        publisher.publishEvent(new UpdateEmailEvent(userId, email, username));
        log.info("Updated email for user {} to: {}", userId, email);

        return SuccessResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully updated username")
                .build();
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }


    @Transactional
    public User createUser(UserRegistrationRequest request) {

        var username = request.getUsername().trim();
        var email = request.getEmail().trim();

        if (userRepository.existsByEmail(email)) {
            throw new ValidationException("Email already in use");
        }

        if (userRepository.existsByUsername(username)) {
            throw new ValidationException("Username already taken");
        }

        var user = User.builder()
                .emailVerifiedAt(null)
                .password(passwordEncoder.encode(request.getPassword()))
                .username(username)
                .role(Role.USER)
                .email(email)
                .build();
        return userRepository.save(user);

    }

    @Transactional
    public void markEmailVerified(UUID userId) {
        userRepository.markUserAsVerified(userId, DateUtil.now());
    }


    public User createGoogleUser(GoogleUserInfoResponse response) {
        var user = User.builder()
                .emailVerifiedAt(DateUtil.now())
                .username(generateTemporaryUsername(response.getEmail()))
                .email(response.getEmail())
                .password(passwordEncoder.encode(generateRandomPassword()))
                .build();

        return userRepository.save(user);
    }

    private CurrentUser buildCurrentUser(User user) {

        var profile = user.getProfile();
        var userProfile = CurrentUser.CurrentUserProfile.builder()
                .displayName(profile.getDisplayName())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .fullName(profile.getFirstName() + " " + profile.getLastName())
                .avatarUrl(profile.getAvatarUrl())
                .avatarPublicId(profile.getAvatarPublicId())
                .gender(profile.getGender())
                .build();

        return CurrentUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .emailVerified(user.getIsEmailVerified())
                .profile(userProfile)
                .build();
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