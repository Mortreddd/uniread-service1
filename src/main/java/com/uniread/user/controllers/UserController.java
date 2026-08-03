package com.uniread.user.controllers;

import com.uniread.auth.exceptions.InvalidTokenException;
import com.uniread.common.dto.api.SuccessResponse;
import com.uniread.user.dto.request.SetupUsernameRequest;
import com.uniread.user.dto.request.UpdateEmailRequest;
import com.uniread.user.dto.request.UpdateUsernameRequest;
import com.uniread.user.dto.request.UserFilter;
import com.uniread.user.dto.response.CurrentUser;
import com.uniread.user.dto.response.UserDto;
import com.uniread.user.dto.response.UserSearchDto;
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
        Page<UserDto> users = userService.getUsers(userDetails, filter);
        return ResponseEntity.ok()
                .body(users);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<Page<UserSearchDto>> searchUsers(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute UserFilter filter
    ) {

        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");
        var publicUsers = userService.searchUsers(userDetails, filter);
        return ResponseEntity.ok(publicUsers);
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

}
