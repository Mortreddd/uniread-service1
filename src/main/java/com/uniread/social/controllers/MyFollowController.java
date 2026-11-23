package com.uniread.social.controllers;

import com.uniread.auth.core.RateLimited;
import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.auth.exceptions.InvalidTokenException;
import com.uniread.social.service.MyFollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/me/social")
public class MyFollowController {

    private final MyFollowService myFollowService;

    @RateLimited
    @PostMapping(path = "/follow/{userId}")
    public ResponseEntity<Void> followUser(
            @PathVariable("userId") UUID userId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");

        myFollowService.followUser(userDetails, userId);
        return ResponseEntity.ok().build();
    }

    @RateLimited
    @DeleteMapping(path = "/unfollow/{userId}")
    public ResponseEntity<Void> unfollowUser(
            @PathVariable("userId") UUID userId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");

        myFollowService.unfollowUser(userDetails, userId);
        return ResponseEntity.ok().build();
    }
}
