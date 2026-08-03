package com.uniread.user.service;

import com.uniread.common.exceptions.ResourceNotFoundException;
import com.uniread.common.services.CloudinaryService;
import com.uniread.user.domain.entities.UserProfile;
import com.uniread.user.dto.request.UpdateUserProfileRequest;
import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.user.dto.response.ProfileDetailsDto;
import com.uniread.user.dto.response.UserProfileDto;
import com.uniread.user.repositories.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserProfilePhotoService profilePhotoService;
    private final UserProfileRepository profileRepository;


    public ProfileDetailsDto getProfile(CustomUserDetails userDetails) {
        var profile = profileRepository.findByUserId(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Unable to retrieve user details"));

        return buildUserProfile(profile);
    }

    @Transactional
    public UserProfileDto updateProfile(
            UpdateUserProfileRequest request,
            CustomUserDetails userDetails
    ) {
        var profile = getProfile(userDetails.getId());

        profile.setBio(request.getBio());
        profile.setDisplayName(request.getDisplayName());
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setGender(request.getGender());

        return buildUserProfileDto(profile);
    }

    @Transactional
    public void updateAvatarPhoto(
            CustomUserDetails userDetails,
            String secureUrl,
            String publicId
    ) {
        var profile = getProfile(userDetails.getId());
        profilePhotoService.deleteAvatar(profile);

        profile.setAvatarUrl(secureUrl);
        profile.setAvatarPublicId(publicId);
        profileRepository.save(profile);
    }

    @Transactional
    public void updateCoverPhoto(
            CustomUserDetails userDetails,
            String secureUrl,
            String publicId
    ) {
        var profile = getProfile(userDetails.getId());
        profilePhotoService.deleteCoverPhoto(profile);

        profile.setCoverUrl(secureUrl);
        profile.setCoverPublicId(publicId);
        profileRepository.save(profile);
    }

    private UserProfile getProfile(UUID userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find the profile"));
    }

    private ProfileDetailsDto buildUserProfile(UserProfile profile) {
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

    private UserProfileDto buildUserProfileDto(UserProfile profile) {
        return UserProfileDto.builder()
                .displayName(profile.getDisplayName())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .bio(profile.getBio())
                .gender(profile.getGender())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}
