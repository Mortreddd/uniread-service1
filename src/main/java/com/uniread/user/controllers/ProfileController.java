package com.uniread.user.controllers;

import com.uniread.auth.exceptions.InvalidTokenException;
import com.uniread.user.dto.request.UpdateUserPhotoRequest;
import com.uniread.user.dto.request.UpdateUserProfileRequest;
import com.uniread.user.dto.response.CurrentUser;
import com.uniread.user.dto.response.ProfileDetailsDto;
import com.uniread.user.dto.response.UserProfileDto;
import com.uniread.user.service.ProfileService;
import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/me")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<CurrentUser> getCurrentUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if(customUserDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");

        CurrentUser currentUser = userService.getCurrentUser(customUserDetails);
        return ResponseEntity.ok()
                .body(currentUser);
    }

    @GetMapping(path = "/profile")
    public ResponseEntity<ProfileDetailsDto> getUserProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");

        var profile = profileService.getProfile(userDetails);
        return ResponseEntity.ok(profile);
    }

    @PatchMapping(path = "/profile/avatar")
    public ResponseEntity updateAvatarPhoto(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateUserPhotoRequest request
    ) {
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");
        profileService.updateAvatarPhoto(userDetails, request.getSecureUrl(), request.getPublicId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping(path = "/profile/cover")
    public ResponseEntity updateCoverPhoto(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateUserPhotoRequest request
    ) {
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");
        profileService.updateCoverPhoto(userDetails, request.getSecureUrl(), request.getPublicId());
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/profile")
    public ResponseEntity<UserProfileDto> updateProfile(
            @Valid @RequestBody UpdateUserProfileRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");

        var response = profileService.updateProfile(request, userDetails);
        return ResponseEntity.ok().body(response);
    }


}
