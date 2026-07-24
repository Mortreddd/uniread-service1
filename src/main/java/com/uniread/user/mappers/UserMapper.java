package com.uniread.user.mappers;

import com.uniread.user.dto.response.UserDto;
import com.uniread.user.domain.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

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
                .firstName(user.getProfile() != null ? user.getProfile().getFirstName() : null)
                .lastName(user.getProfile() != null ? user.getProfile().getLastName() : null)
                .displayName(user.getProfile() != null ? user.getProfile().getDisplayName() : null)
                .gender(user.getProfile() != null ? user.getProfile().getGender() : null)
                .avatarUrl(user.getProfile() != null ? user.getProfile().getAvatarPhoto() : null)
                .build();
    }
}