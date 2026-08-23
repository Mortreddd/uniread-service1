package com.uniread.admin.mapper;

import com.uniread.admin.dto.response.UserMonitoringDto;
import com.uniread.auth.domain.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserMapper {

    public UserMonitoringDto toUserMonitor(User user) {
        var profile = user.getProfile();
        var profileDto = UserMonitoringDto.UserProfileMonitor.builder()
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
        return UserMonitoringDto.builder()
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
}
