package com.bsit.uniread.application.controllers.user;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.api.SuccessResponse;
import com.bsit.uniread.application.dto.request.user.UserProfileFilter;
import com.bsit.uniread.application.dto.response.book.BookDto;
import com.bsit.uniread.application.dto.response.profile.ProfileDashboardDto;
import com.bsit.uniread.application.services.profile.ProfileService;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = ApiEndpoints.PROFILE)
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PutMapping
    public ResponseEntity<SuccessResponse> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return null;
    }

    @GetMapping(path = "/dashboard")
    public ResponseEntity<ProfileDashboardDto> getUserDashboard(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok()
                .body(profileService.getUserDashboard(userDetails));
    }

}
