package com.uniread.user.controllers;

import com.uniread.auth.exceptions.InvalidTokenException;
import com.uniread.common.dto.api.SuccessResponse;
import com.uniread.user.dto.request.SetupUsernameRequest;
import com.uniread.user.dto.request.UpdateEmailRequest;
import com.uniread.user.dto.request.UpdateUsernameRequest;
import com.uniread.user.dto.request.UserFilter;
import com.uniread.user.dto.response.CurrentUser;
import com.uniread.user.dto.response.UserDto;
import com.uniread.user.service.UserService;
import com.uniread.auth.domain.entities.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDto>> getUsers(
            @ModelAttribute UserFilter filter,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Page<UserDto> users = userService.searchUsers(userDetails, filter);
        return ResponseEntity.ok()
                .body(users);
    }
    /**
     * Update the username of the user
     * @param request
     * @return SuccessResponse
     */
    @PatchMapping(path = "/username")
    public ResponseEntity<SuccessResponse> updateUsername(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateUsernameRequest request
    ) {
        if(userDetails == null) throw new InvalidTokenException("Invalid session");
        var response = userService.updateUsername(userDetails.getId(), request.getUsername());
        return ResponseEntity.ok()
                .body(response);
    }

    @PatchMapping(path = "/email")
    public ResponseEntity<SuccessResponse> updateEmail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateEmailRequest request
    ) {
        if(userDetails == null) throw new InvalidTokenException("Invalid session");
        var response = userService.updateEmail(userDetails.getId(), request.getEmail(), userDetails.getUsername());
        return ResponseEntity.ok().body(response);
    }
    /**
     * Extract the user based on access token or jwt token of the user
     * @param customUserDetails
     * @return User
     */
    @GetMapping(path = "/me")
    public ResponseEntity<CurrentUser> getCurrentUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if(customUserDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");

        CurrentUser currentUser = userService.getCurrentUser(customUserDetails.getId());
        return ResponseEntity.ok()
                .body(currentUser);
    }
}
