package com.bsit.uniread.application.controllers.user;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.api.SuccessResponse;
import com.bsit.uniread.application.dto.response.follow.FollowDto;
import com.bsit.uniread.application.services.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Api Endpoint - /api/v1/users/{currentUserId}/follow
 */
@RestController
@RequestMapping(path = ApiEndpoints.FOLLOW)
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
    @GetMapping(path = "/followings")
    public ResponseEntity<Page<FollowDto>> getUserFollowings(
            @PathVariable(name = "currentUserId") UUID userId,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "query", required = false) String query
    ) {
        Page<FollowDto> follows = followService.getFollowingsByUserId(pageNo, pageSize, userId, query).map(FollowDto::new);
        return ResponseEntity.ok()
                .body(follows);
    }

    /**
     * Get the followers of user
     * @param userId
     * @param pageNo
     * @param pageSize
     * @param query
     * @return page of followers
     */
    @GetMapping(path = "/followers")
    public ResponseEntity<Page<FollowDto>> getUserFollowers(
            @PathVariable(name = "currentUserId") UUID userId,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "query", required = false) String query
    ) {
        Page<FollowDto> follows = followService.getFollowersByUserId(pageNo, pageSize, userId, query)
                .map(FollowDto::new);
        return ResponseEntity.ok()
                .body(follows);
    }

    /**
     * Create a new follow transaction
     * @param currentUserId
     * @param followedUserId
     * @return new follow
     */
    @PostMapping(path = "/{followedUserId}")
    public ResponseEntity<FollowDto> createNewFollowing(
            @PathVariable(name = "currentUserId") UUID currentUserId,
            @PathVariable(name = "followedUserId") UUID followedUserId
    ) {

        FollowDto newFollow = new FollowDto(followService.createFollow(currentUserId, followedUserId));
        return ResponseEntity.ok()
                .body(newFollow);
    }

    /**
     * Delete the record of currentUser following unfollowedUser
     * @param currentUserId
     * @param unfollowedUserId
     * @return SuccessResponse
     */
    @DeleteMapping(path = "/{unfollowedUserId}/delete")
    public ResponseEntity<SuccessResponse> unfollowUser(
            @PathVariable(name = "currentUserId") UUID currentUserId,
            @PathVariable(name = "unfollowedUserId") UUID unfollowedUserId
    ) {

        followService.unfollowUser(currentUserId, unfollowedUserId);
        SuccessResponse response = SuccessResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully unfollowed the user")
                .build();

        return ResponseEntity.ok().body(response);
    }

}
