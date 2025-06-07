package com.bsit.uniread.application.services;

import com.bsit.uniread.infrastructure.handler.exceptions.follow.UserNotFollowedException;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.handler.exceptions.follow.AlreadyFollowingUserException;
import com.bsit.uniread.infrastructure.handler.publishers.follower.FollowPublisher;
import com.bsit.uniread.infrastructure.repositories.FollowRepository;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;
    private final FollowPublisher followPublisher;

    @Transactional(readOnly = true)
    public Page<Follow> getFollowsByUserId(int pageNo, int pageSize, UUID userId, String query) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        User user = userService.getUserById(userId);

        if(StringUtil.isNullOrEmpty(query)) {
            return followRepository.findByFollowingOrFollowerOrFollowerFirstNameContainingIgnoreCaseOrFollowerLastNameContainingIgnoreCaseOrFollowerUsernameContainingIgnoreCaseOrFollowingFirstNameContainingIgnoreCaseOrFollowingLastNameContainingIgnoreCaseOrFollowingUsernameContainingIgnoreCase(user, user, query, query, query, query, query, query, pageable);
        }

        return followRepository.findByFollowingOrFollower(user, user, pageable);
    }
    /**
     * Get the followers of the user or get the users matching query
     * @param pageNo
     * @param pageSize
     * @param userId
     * @param query
     * @return Pagination of Follow
     */
    @Transactional(readOnly = true)
    public Page<Follow> getFollowersByUserId(int pageNo, int pageSize, UUID userId, String query) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        User user = userService.getUserById(userId);

        if(StringUtil.isNullOrEmpty(query)) {
            return followRepository.findByFollowing(user, pageable);
        }

        return followRepository.findByFollowingAndFollowerFirstNameContainingIgnoreCaseOrFollowerLastNameContainingIgnoreCaseOrFollowerUsernameContainingIgnoreCase(user, query, query, query, pageable);
    }

    /**
     * Get the followings of the User
     * @param pageNo
     * @param pageSize
     * @param userId
     * @param query
     * @return Pagination of Follow
     */
    public Page<Follow> getFollowingsByUserId(int pageNo, int pageSize, UUID userId, String query) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        User user = userService.getUserById(userId);

        if(StringUtil.isNullOrEmpty(query)) {
            return followRepository.findByFollower(user, pageable);
        }

        return followRepository.findByFollowerAndFollowingFirstNameContainingIgnoreCaseOrFollowingLastNameContainingIgnoreCaseOrFollowingUsernameContainingIgnoreCase(user, query, query, query, pageable);
    }

    /**
     * Create a new follow transaction
     * @param requesterId
     * @param followedUserId
     * @return follow
     */
    public Follow createFollow(UUID requesterId, UUID followedUserId) {
        User userRequester = userService.getUserById(requesterId);
        User followedUser = userService.getUserById(followedUserId);

        // Checks if the requester is already followed and throws an exception if not
        if(isFollowingUser(userRequester, followedUser)) {
            throw new AlreadyFollowingUserException("Already following user");
        }

        Follow newFollower = followRepository.save(Follow.builder()
                .follower(userRequester)
                .following(followedUser)
                .createdAt(DateUtil.now())
                .build()
        );

        followPublisher.publishNewFollower(newFollower);

        return newFollower;
    }

    /**
     * Delete the follow and create an unfollow transaction for userRequester to followedUser
     * @param currentUserId
     * @param unfollowedUserId
     */
    @Transactional
    public void unfollowUser(UUID currentUserId, UUID unfollowedUserId) {
        User userRequester = userService.getUserById(currentUserId);
        User unfollowedUser = userService.getUserById(unfollowedUserId);

        // Throws an exception if requester is not followed
        if(!isFollowingUser(userRequester, unfollowedUser)) {
            throw new UserNotFollowedException("User is not followed");
        }

        int rowsAffected = followRepository.deleteByFollowerAndFollowing(userRequester, unfollowedUser);
        log.info("Rows affected {}", rowsAffected);
    }

    /**
     * Checks if the userRequester is followed the followed user and if it was existed
     * @param userRequester
     * @param followedUser
     * @return boolean
     */
    public Boolean isFollowingUser(User userRequester, User followedUser) {
        boolean isFollowing = followRepository.existsByFollowerAndFollowing(userRequester, followedUser);
        log.debug("{} is following {} = {}", userRequester.getUsername(), followedUser.getUsername(), isFollowing);
        return isFollowing;
    }

    public Boolean isMutual(User requester, User following) {
        return followRepository.existsByFollowerAndFollowingAndFollowingAndFollower(requester, following, following, requester);
    }
}
