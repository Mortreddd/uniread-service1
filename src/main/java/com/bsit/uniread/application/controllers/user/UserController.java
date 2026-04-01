package com.bsit.uniread.application.controllers.user;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.api.SuccessResponse;
import com.bsit.uniread.application.dto.request.user.SetupUsernameRequest;
import com.bsit.uniread.application.dto.request.user.UserFilter;
import com.bsit.uniread.application.dto.response.user.CurrentUser;
import com.bsit.uniread.application.dto.response.user.UserDto;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = ApiEndpoints.USERS)
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

//    /**
//     * Get the user info based in given id
//     * @param userId
//     * @return user
//     */
//    @GetMapping(path = "/{userId}")
//    public ResponseEntity<AuthorDto> getUserById(
//            @PathVariable(name = "userId") UUID userId
//    ) {
//        AuthorDto user = userService.getUserById(userId));
//        return ResponseEntity.ok()
//                .body(user);
//    }

    /**
     * Update the username of the user
     * @param request
     * @return SuccessResponse
     */
    @PutMapping(path = "/{userId}/setup/username")
    public ResponseEntity<SuccessResponse> setupUsername(
            @PathVariable(name = "userId") UUID userId,
            @Valid @RequestBody SetupUsernameRequest request
    ) {
        userService.updateUsername(userId, request.getUsername());
        SuccessResponse response = SuccessResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Username has been updated")
                .build();
        return ResponseEntity.ok()
                .body(response);
    }


    /**
     * Extract the user based on access token or jwt token of the user
     * @param customUserDetails
     * @return User
     */
    @GetMapping(path = "/me")
    public ResponseEntity<CurrentUser> getCurrentUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        CurrentUser currentUser = userService.getCurrentUser(customUserDetails.getId());
        return ResponseEntity.ok()
                .body(currentUser);
    }
}
