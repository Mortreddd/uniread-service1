package com.uniread.social.service;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.social.domain.events.NewFollowerEvent;
import com.uniread.social.dto.response.FollowDto;
import com.uniread.common.exceptions.ResourceNotFoundException;
import com.uniread.social.domain.entities.Follow;
import com.uniread.auth.domain.entities.User;
import com.uniread.social.exceptions.AlreadyFollowingUserException;
import com.uniread.social.exceptions.UserNotFollowedException;
import com.uniread.social.repositories.FollowRepository;
import com.uniread.user.repositories.UserProfileRepository;
import com.uniread.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyFollowService {

    private final ApplicationEventPublisher publisher;
    private final FollowValidator validator;
    private final FollowRepository followRepository;
    private final UserProfileRepository userProfileRepository;


    @Transactional
    public void followUser(CustomUserDetails userDetails, UUID followingId) {
        validator.validateNotSelfFollow(userDetails.getId(), followingId);
        validator.validateUsersExists(userDetails.getId(), followingId);
        validator.validateNotAlreadyFollowing(userDetails.getId(), followingId);

        var follower = User.builder().id(userDetails.getId()).build();
        var following = User.builder().id(followingId).build();

        var follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        userProfileRepository.increaseFollowing(userDetails.getId());
        userProfileRepository.increaseFollower(followingId);
        followRepository.save(follow);
        publisher.publishEvent(new NewFollowerEvent(followingId, userDetails.getUsername(), userDetails.getDisplayName()));
        log.info("User {} followed {}", userDetails.getId(), followingId);


    }

    @Transactional
    public void unfollowUser(CustomUserDetails userDetails, UUID followingId) {
        validator.validateNotSelfFollow(userDetails.getId(), followingId);
        validator.validateUsersExists(userDetails.getId(), followingId);
        validator.validateNotFollowing(userDetails.getId(), followingId);

        userProfileRepository.decreaseFollower(userDetails.getId());
        userProfileRepository.decreaseFollowing(followingId);
        followRepository.deleteByFollowerIdAndFollowingId(userDetails.getId(), followingId);
        log.info("User {} unfollowed {}", userDetails.getId(), followingId);
    }

}
