package com.uniread.user.service;

import com.uniread.auth.dto.response.GoogleUserInfoResponse;
import com.uniread.user.domain.entities.Gender;
import com.uniread.user.domain.entities.User;
import com.uniread.user.domain.entities.UserProfile;
import com.uniread.user.repositories.UserProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;


    @Transactional
    public UserProfile createUserProfile(User user, GoogleUserInfoResponse userInfo) {
        UserProfile profile = UserProfile.builder()
                .firstName(userInfo.getGivenName())
                .lastName(userInfo.getFamilyName())
                .gender(Gender.OTHER)
                .avatarPhoto(userInfo.getPicture())
                .user(user)
                .build();

        UserProfile savedProfile = userProfileRepository.save(profile);
        log.info("Created user profile for user: {}", user.getEmail());

        return savedProfile;
    }

}
