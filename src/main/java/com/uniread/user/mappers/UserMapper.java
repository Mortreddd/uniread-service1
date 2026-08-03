package com.uniread.user.mappers;

import com.uniread.user.dto.response.ProfileDetailsDto;
import com.uniread.user.dto.response.UserDto;
import com.uniread.user.domain.entities.User;
import com.uniread.user.dto.response.UserSearchDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    /**
     * The user is eager loaded a profile so it won't cause N + 1 query issue
     * @param user
     * @return UserDto
     */
    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        var profile = user.getProfile();
        var profileDto = ProfileDetailsDto.builder()
                .id(profile.getId())
                .userId(user.getId())
                .firstName(profile.getFirstName())
                .lastName(profile.getDisplayName())
                .displayName(profile.getDisplayName())
                .gender(profile.getGender())
                .bio(profile.getBio())
                .avatarUrl(profile.getAvatarUrl())
                .avatarPublicId(profile.getCoverPublicId())
                .coverUrl(profile.getCoverUrl())
                .coverPublicId(profile.getCoverPublicId())
                .build();
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .emailVerifiedAt(user.getEmailVerifiedAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .bannedAt(user.getBannedAt())
                .unbannedAt(user.getUnbannedAt())
                .deletedAt(user.getDeletedAt())
                .isEmailVerified(user.getEmailVerifiedAt() != null)
                // Profile fields
                .profile(profileDto)
                .build();
    }

    /**
     * The user is eager loaded a profile so it won't cause N + 1 query issue
     * @param user
     * @return
     */
    public UserSearchDto toSearchDto(User user) {
        if(user == null) return null;

        var profile = user.getProfile();
        return UserSearchDto.builder()
                .id(user.getId())
                .displayName(profile.getDisplayName())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .avatarUrl(profile.getAvatarUrl())
                .username(user.getUsername())
                .isEmailVerified(user.getIsEmailVerified())
                .build();

    }
}