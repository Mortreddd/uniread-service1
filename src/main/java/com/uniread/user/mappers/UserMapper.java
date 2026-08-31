package com.uniread.user.mappers;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.common.services.CloudinaryService;
import com.uniread.user.dto.response.CurrentUser;
import com.uniread.user.dto.response.ProfileDetailsDto;
import com.uniread.user.dto.response.UserDto;
import com.uniread.auth.domain.entities.User;
import com.uniread.user.dto.response.UserSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final CloudinaryService cloudinaryService;
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
        var avatarUrl = cloudinaryService.generatePublicUrl(profile.getAvatarPublicId());
        var coverUrl = cloudinaryService.generatePublicUrl(profile.getCoverPublicId());

        var profileDto = ProfileDetailsDto.builder()
                .id(profile.getId())
                .userId(user.getId())
                .firstName(profile.getFirstName())
                .lastName(profile.getDisplayName())
                .displayName(profile.getDisplayName())
                .gender(profile.getGender())
                .bio(profile.getBio())
                .avatarUrl(avatarUrl)
                .avatarPublicId(profile.getCoverPublicId())
                .coverUrl(coverUrl)
                .coverPublicId(profile.getCoverPublicId())
                .build();
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
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

    public CurrentUser toCurrentUser(User user, CustomUserDetails userDetails) {

        var profile = user.getProfile();
        var avatarUrl = cloudinaryService.generatePublicUrl(profile.getAvatarPublicId());
        var userProfile = CurrentUser.CurrentUserProfile.builder()
                .displayName(profile.getDisplayName())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .fullName(profile.getFirstName() + " " + profile.getLastName())
                .avatarUrl(avatarUrl)
                .avatarPublicId(profile.getAvatarPublicId())
                .gender(profile.getGender())
                .build();

        return CurrentUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .hasAdminAccess(!userDetails.getAuthorities().isEmpty())
                .emailVerified(user.isEmailVerified())
                .profile(userProfile)
                .build();
    }

    public CurrentUser toCurrentUser(User user) {

        var profile = user.getProfile();
        var avatarUrl = cloudinaryService.generatePublicUrl(profile.getAvatarPublicId());
        var userProfile = CurrentUser.CurrentUserProfile.builder()
                .displayName(profile.getDisplayName())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .fullName(profile.getFirstName() + " " + profile.getLastName())
                .avatarUrl(avatarUrl)
                .avatarPublicId(profile.getAvatarPublicId())
                .gender(profile.getGender())
                .build();

        return CurrentUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .hasAdminAccess(false)
                .emailVerified(user.isEmailVerified())
                .profile(userProfile)
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
        var avatarUrl = cloudinaryService.generatePublicUrl(profile.getAvatarPublicId());
        return UserSearchDto.builder()
                .id(user.getId())
                .displayName(profile.getDisplayName())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .avatarUrl(avatarUrl)
                .username(user.getUsername())
                .isEmailVerified(user.isEmailVerified())
                .build();

    }
}