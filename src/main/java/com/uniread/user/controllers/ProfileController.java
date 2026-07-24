package com.uniread.user.controllers;

import com.uniread.common.dto.api.SuccessResponse;
import com.uniread.user.dto.response.ProfileDashboardDto;
import com.uniread.user.service.ProfileService;
import com.uniread.auth.domain.entities.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PutMapping
    public ResponseEntity<SuccessResponse> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return null;
    }

}
