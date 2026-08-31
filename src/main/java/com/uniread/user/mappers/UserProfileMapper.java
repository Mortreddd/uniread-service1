package com.uniread.user.mappers;

import com.uniread.user.domain.entities.UserProfile;
import com.uniread.user.dto.response.ProfileDetailsDto;
import com.uniread.user.dto.response.UserProfileDto;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {

    public UserProfileDto toUserProfileDto(UserProfile profile) {
        return UserProfileDto.builder()
                .displayName(profile.getDisplayName())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .bio(profile.getBio())
                .gender(profile.getGender())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
    public ProfileDetailsDto toDetail(UserProfile profile) {
        return ProfileDetailsDto.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .displayName(profile.getDisplayName())
                .gender(profile.getGender())
                .bio(profile.getBio())
                .avatarUrl(profile.getAvatarUrl())
                .avatarPublicId(profile.getAvatarPublicId())
                .coverUrl(profile.getCoverUrl())
                .coverPublicId(profile.getCoverPublicId())
                .build();
    }
}
