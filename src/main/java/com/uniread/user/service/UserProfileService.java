package com.uniread.user.service;

import com.uniread.auth.dto.response.GoogleUserInfoResponse;
import com.uniread.common.exceptions.ResourceNotFoundException;
import com.uniread.user.domain.entities.Gender;
import com.uniread.auth.domain.entities.User;
import com.uniread.user.domain.entities.UserProfile;
import com.uniread.user.repositories.UserProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;


    public UserProfile getUserProfileById(UUID userId) {
        return userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to load the user details"));
    }

    @Transactional
    public UserProfile createUserProfile(User user, GoogleUserInfoResponse userInfo) {
        UserProfile profile = UserProfile.builder()
                .firstName(userInfo.getGivenName())
                .lastName(userInfo.getFamilyName())
                .gender(Gender.OTHER)
                .user(user)
                .build();

        UserProfile savedProfile = userProfileRepository.save(profile);
        log.info("Created user profile for user: {}", user.getEmail());

        return savedProfile;
    }

}
