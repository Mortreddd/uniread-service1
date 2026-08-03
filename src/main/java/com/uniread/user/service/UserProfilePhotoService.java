package com.uniread.user.service;

import com.uniread.common.services.CloudinaryService;
import com.uniread.user.domain.entities.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfilePhotoService {

    private final CloudinaryService cloudinaryService;

    public void deleteAvatar(UserProfile profile) {
        cloudinaryService.delete(profile.getAvatarPublicId());
    }

    public void deleteCoverPhoto(UserProfile profile) {
        cloudinaryService.delete(profile.getCoverPublicId());
    }

}
