package com.bsit.uniread.application.controllers.user;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.api.SuccessResponse;
import com.bsit.uniread.application.dto.response.follow.FollowDto;
import com.bsit.uniread.application.dto.response.follow.FollowUserDto;
import com.bsit.uniread.application.services.FollowService;
import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import com.bsit.uniread.domain.entities.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Api Endpoint - /api/v1/users
 */
@RestController
@RequestMapping(path = ApiEndpoints.USERS)
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    /**
     * Get the followings of user
     * @param userId
     * @param pageNo
     * @param pageSize
     * @param query
     * @return page of followings
     */
    @GetMapping(path = "/{userId}/follow/followings")
    public ResponseEntity<Page<FollowUserDto>> getUserFollowings(
            @PathVariable(name = "userId") UUID userId,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "query", required = false) String query,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Page<User> users = followService.getFollowingsByUserId(userId, pageNo, pageSize, query)
                .map(Follow::getFollowing);
        List<Follow> follows = userDetails == null ? List.of() : followService.getCurrentUserFollows(userDetails);
        Page<FollowUserDto> followings = users
                .map(u -> {

                    boolean isFollowing = false;
                    boolean isFollower = false;

                    if(userDetails != null) {

                        isFollowing = follows
                                .stream()
                                .anyMatch(follow -> follow.getFollowing().getId().equals(u.getId()));

                        isFollower = follows
                                .stream()
                                .anyMatch(follow -> follow.getFollower().getId().equals(u.getId()));

                    }
                    boolean isMutualFollowing = isFollowing && isFollower;
                    return new FollowUserDto(u, isFollowing, isMutualFollowing);
                });

        return ResponseEntity.ok()
                .body(followings);
    }

    /**
     * Get the followers of user
     * @param userId
     * @param pageNo
     * @param pageSize
     * @param query
     * @return page of followers
     */
    @GetMapping(path = "/{userId}/follow/followers")
    public ResponseEntity<Page<FollowUserDto>> getUserFollowers(
            @PathVariable(name = "userId") UUID userId,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "query", required = false) String query,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Page<User> users = followService.getFollowersByUserId(userId, pageNo, pageSize, query)
                .map(Follow::getFollower);
        List<Follow> follows = userDetails == null ? List.of() : followService.getCurrentUserFollows(userDetails);
        Page<FollowUserDto> followers = users
                .map(u -> {

                    boolean isFollowing = false;
                    boolean isFollower = false;
                    if(userDetails != null) {

                        isFollowing = follows
                                .stream()
                                .anyMatch(follow -> follow.getFollowing().getId().equals(u.getId()));

                        isFollower = follows
                                .stream()
                                .anyMatch(follow -> follow.getFollower().getId().equals(u.getId()));

                    }

                    boolean isMutualFollowing = isFollowing && isFollower;
                    return new FollowUserDto(u, isFollowing, isMutualFollowing);
                });

        return ResponseEntity.ok()
                .body(followers);
    }

    /**
     * Create a new follow transaction
     * @param followedUserId
     * @param userDetails
     * @return Follow
     */
    @PostMapping(path = "/{userId}/follow")
    public ResponseEntity<FollowDto> createNewFollowing(
            @PathVariable(name = "userId") UUID followedUserId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        FollowDto newFollow = new FollowDto(followService.createFollow(userDetails.getId(), followedUserId));
        return ResponseEntity.ok()
                .body(newFollow);
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
        SuccessResponse response = SuccessResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully unfollowed the user")
                .build();

        return ResponseEntity.ok().body(response);
    }

}
