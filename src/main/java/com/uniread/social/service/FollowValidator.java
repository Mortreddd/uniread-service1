package com.uniread.social.service;

import com.uniread.common.exceptions.ResourceNotFoundException;
import com.uniread.social.exceptions.AlreadyFollowingUserException;
import com.uniread.social.exceptions.UserNotFollowedException;
import com.uniread.social.repositories.FollowRepository;
import com.uniread.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowValidator {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;


    public Boolean isFollowing(UUID followerId, UUID followingId) {
        return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    public Boolean isFollower(UUID followingId, UUID followerId) {
        return isFollowing(followingId, followerId);
    }

    public void validateUserExists(UUID userId) {
        if(!userRepository.existsById(userId)) {
            log.error("User not found with ID: {}", userId);
            throw new ResourceNotFoundException("User not found");
        }
    }

    public void validateUsersExists(UUID followerId, UUID followingId) {
        if(!userRepository.existsById(followerId)) {
            log.error("Follower User is not found with ID : {}", followerId);
            throw new ResourceNotFoundException("Follower is not found");
        }

        if(!userRepository.existsById(followingId)) {
            log.error("Following User is not found with ID : {}", followingId);
            throw new ResourceNotFoundException("Following is not found");
        }
    }

    public void validateNotSelfFollow(UUID followerId, UUID followingId) {
        if(followerId.equals(followingId)) {
            log.error("User cannot follow themselves");
            throw new IllegalArgumentException("User cannot follow themselves");
        }
    }

    public void validateNotAlreadyFollowing(UUID followerId, UUID followingId) {
        if(isFollowing(followerId, followingId)) {
            log.error("User {} already following {} ", followerId, followingId);
            throw new AlreadyFollowingUserException(String.format("User %s already following %s", followerId, followingId));
        }
    }

    public void validateNotFollowing(UUID followerId, UUID followingId) {
        if(!isFollowing(followerId, followingId)) {
            log.error("User {} is not following {}", followerId, followingId);
            throw new UserNotFollowedException(String.format("User %s is not following %s", followerId, followingId));
        }
    }
}
