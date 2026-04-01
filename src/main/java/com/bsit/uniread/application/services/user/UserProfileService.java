package com.bsit.uniread.application.services.user;

import com.bsit.uniread.application.dto.response.auth.GoogleUserInfoResponse;
import com.bsit.uniread.domain.entities.user.Gender;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.domain.entities.user.UserProfile;
import com.bsit.uniread.infrastructure.repositories.user.UserProfileRepository;
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
        log.debug("Created user profile for user: {}", user.getEmail());

        return savedProfile;
    }

}
