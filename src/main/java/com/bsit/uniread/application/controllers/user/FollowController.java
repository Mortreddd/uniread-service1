package com.bsit.uniread.application.controllers.user;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.api.SuccessResponse;
import com.bsit.uniread.application.dto.request.follow.FollowerFilter;
import com.bsit.uniread.application.dto.request.follow.FollowingFilter;
import com.bsit.uniread.application.dto.response.follow.AuthUserFollowDto;
import com.bsit.uniread.application.dto.response.follow.FollowDto;
import com.bsit.uniread.application.dto.response.follow.FollowUserDto;
import com.bsit.uniread.application.services.follow.FollowService;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Api Endpoint - /api/v1/users
 */
@RestController
@RequestMapping(path = ApiEndpoints.USERS)
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @GetMapping(path = "/{userId}/followings")
    @Operation(summary = "Get the followings of specified user", description = "Get the followings of specified user which supports filtering")
    public ResponseEntity<Page<FollowUserDto>> getUserFollowings(
            @PathVariable(name = "userId") UUID userId,
            @ModelAttribute FollowingFilter filter,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        var authUserId = userDetails != null ? userDetails.getId() : null;
        var followings = followService.getUserFollowings(userId, authUserId, filter);

        return ResponseEntity.ok()
                .body(followings);
    }

    @GetMapping(path = "/{userId}/followers")
    @Operation(summary = "Get the followers of specified user", description = "Get the followers of specified user which supports filtering")
    public ResponseEntity<Page<FollowUserDto>> getUserFollowers(
            @PathVariable(name = "userId") UUID userId,
            @ModelAttribute FollowerFilter filter,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        var authUserId = userDetails != null ? userDetails.getId() : null;
        var followers = followService.getUserFollowers(userId, authUserId, filter);

        return ResponseEntity.ok()
                .body(followers);
    }

    @GetMapping(path = "/me/followings")
    @Operation(summary = "Get the followings of auth user", description = "Get the followings of auth user which supports filtering")
    public ResponseEntity<Page<AuthUserFollowDto>> getAuthUserFollowings(
        @ModelAttribute FollowingFilter filter,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        var authUserId = userDetails != null ? userDetails.getId() : null;
        var followings = followService.getAuthUserFollowings(authUserId, filter);
        return ResponseEntity.ok().body(followings);
    }

    @GetMapping(path = "/me/followers")
    @Operation(summary = "Get the followers of auth user", description = "Get the followers of auth user which supports filtering")
    public ResponseEntity<Page<AuthUserFollowDto>> getAuthUserFollowers(
            @ModelAttribute FollowerFilter filter,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        var authUserId = userDetails != null ? userDetails.getId() : null;
        var followers = followService.getAuthUserFollowers(authUserId, filter);
        return ResponseEntity.ok().body(followers);
    }

    @PostMapping(path = "/{userId}/follow")
    @Operation(summary = "Auth user creates follow to given user")
    public ResponseEntity<FollowDto> createNewFollowing(
            @PathVariable(name = "userId") UUID followedUserId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        var follow = followService.createFollow(userDetails.getId(), followedUserId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(follow);
    }

    /**
     * Delete the record of currentUser following unfollowedUser
     * @param unfollowedUserId
     * @param userDetails
     * @return SuccessResponse
     */
    @DeleteMapping(path = "/{userId}/follow")
    public ResponseEntity<SuccessResponse> unfollowUser(
            @PathVariable(name = "userId") UUID unfollowedUserId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        followService.unfollowUser(userDetails.getId(), unfollowedUserId);
        return ResponseEntity.ok().build();
    }

}
