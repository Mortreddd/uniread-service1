package com.bsit.uniread.application.controllers.user;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.api.SuccessResponse;
import com.bsit.uniread.application.dto.request.follow.NewFollowerRequest;
import com.bsit.uniread.application.dto.response.follow.FollowDto;
import com.bsit.uniread.application.services.FollowService;
import com.bsit.uniread.domain.entities.Follow;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Api Endpoint - /api/v1/users/{userId}/follow
 */
@RestController
@RequestMapping(path = ApiEndpoints.FOLLOW)
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @GetMapping(path = "/followings")
    public ResponseEntity<Page<FollowDto>> getUserFollowings(
            @PathVariable(name = "userId") UUID userId,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "query", required = false) String query
    ) {
        Page<FollowDto> follows = followService.getFollowingsByUserId(pageNo, pageSize, userId, query).map(FollowDto::new);
        return ResponseEntity.ok()
                .body(follows);
    }

    @PostMapping(path = "/new")
    public ResponseEntity<FollowDto> createNewFollowing(
            @PathVariable(name = "userId") UUID userId,
            @Valid @RequestBody NewFollowerRequest newFollowerRequest
    ) {
        FollowDto newFollow = new FollowDto(followService.createFollow(newFollowerRequest.getRequesterId(), userId));
        return ResponseEntity.ok()
                .body(newFollow);   
    }

    @DeleteMapping(path = "/unfollow/{requesterId}")
    public ResponseEntity<SuccessResponse> unfollowUser(
            @PathVariable(name = "userId") UUID userId,
            @PathVariable(name = "requesterId") UUID requesterId
    ) {

        followService.unfollowUser(userId, requesterId);
        SuccessResponse response = SuccessResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully unfollowed the user")
                .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping(path = "/followers")
    public ResponseEntity<Page<FollowDto>> getUserFollowers(
            @PathVariable(name = "userId") UUID userId,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "query", required = false) String query
    ) {
        Page<FollowDto> follows = followService.getFollowersByUserId(pageNo, pageSize, userId, query)
                .map(FollowDto::new);
        return ResponseEntity.ok()
                .body(follows);
    }

}
