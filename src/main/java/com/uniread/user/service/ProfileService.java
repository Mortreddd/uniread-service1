package com.uniread.user.service;

import com.uniread.common.exceptions.ResourceNotFoundException;
import com.uniread.user.domain.entities.UserProfile;
import com.uniread.user.dto.request.UpdateUserAvatarRequest;
import com.uniread.user.dto.request.UpdateUserCoverRequest;
import com.uniread.user.dto.request.UpdateUserProfileRequest;
import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.user.dto.response.ProfileDetailsDto;
import com.uniread.user.dto.response.ProfilePhotoResponse;
import com.uniread.user.dto.response.UserProfileDto;
import com.uniread.user.mappers.UserProfileMapper;
import com.uniread.user.repositories.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserProfileMapper profileMapper;
    private final UserProfilePhotoService profilePhotoService;
    private final UserProfileRepository profileRepository;

    public ProfileDetailsDto getProfile(CustomUserDetails userDetails) {
        var profile = profileRepository.findByUserId(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Unable to retrieve user details"));

        return profileMapper.toDetail(profile);
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

        return profileMapper.toUserProfileDto(profile);
    }

    @Transactional
    public ProfilePhotoResponse updateAvatarPhoto(
            CustomUserDetails userDetails,
            UpdateUserAvatarRequest request
    ) {
        var profile = getProfile(userDetails.getId());

        var uploadResult = profilePhotoService
                .replaceAvatar(
                        request.getAvatar(),
                        profile.getUser().getId(),
                        profile.getAvatarPublicId()
                );
        var publicId = (String) uploadResult.get("public_id");
        var secureUrl = profilePhotoService.generateUrl(publicId);

        profile.setAvatarPublicId(publicId);

        profileRepository.save(profile);
        return ProfilePhotoResponse.builder()
                .publicId(publicId)
                .photoUrl(secureUrl)
                .build();
    }

    @Transactional
    public ProfilePhotoResponse updateCoverPhoto(
            CustomUserDetails userDetails,
            UpdateUserCoverRequest request
    ) {
        var profile = getProfile(userDetails.getId());

        var uploadResult = profilePhotoService
                .replaceCover(
                        request.getCover(),
                        profile.getUser().getId(),
                        profile.getCoverPublicId()
                );
        var secureUrl = (String) uploadResult.get("secure_url");
        var publicId = (String) uploadResult.get("public_id");

        profile.setCoverUrl(secureUrl);
        profile.setCoverPublicId(publicId);
        profileRepository.save(profile);

        return ProfilePhotoResponse.builder()
                .publicId(publicId)
                .photoUrl(secureUrl)
                .build();
    }

    @Transactional
    public void deleteAvatar(CustomUserDetails userDetails) {
        var profile = getProfile(userDetails.getId());
        profilePhotoService.deleteAvatar(profile.getAvatarPublicId());

        profile.setAvatarPublicId(null);
        profile.setAvatarUrl(null);

        profileRepository.save(profile);
    }

    @Transactional
    public void deleteCover(CustomUserDetails userDetails) {
        var profile = getProfile(userDetails.getId());
        profilePhotoService.deleteCoverPhoto(profile.getCoverPublicId());

        profile.setCoverPublicId(null);
        profile.setCoverUrl(null);

        profileRepository.save(profile);
    }

    private UserProfile getProfile(UUID userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find the profile"));
    }

}
