package com.bsit.uniread.application.services.follow;

import com.bsit.uniread.application.dto.request.follow.FollowerFilter;
import com.bsit.uniread.application.dto.request.follow.FollowingFilter;
import com.bsit.uniread.application.dto.response.follow.AuthUserFollowDto;
import com.bsit.uniread.application.dto.response.follow.FollowDto;
import com.bsit.uniread.application.dto.response.follow.FollowUserDto;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.handler.exceptions.follow.AlreadyFollowingUserException;
import com.bsit.uniread.infrastructure.handler.exceptions.follow.UserNotFollowedException;
import com.bsit.uniread.infrastructure.repositories.follow.FollowRepository;
import com.bsit.uniread.infrastructure.repositories.user.UserRepository;
import jakarta.persistence.EntityManager;
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
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public Page<FollowUserDto> getUserFollowers(UUID targetUserId, UUID authUserId, FollowerFilter filter) {
        validateUserExists(targetUserId);

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(filter.getPageNo(), filter.getPageSize(), sort);
        return followRepository.findUserFollowers(targetUserId, authUserId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<FollowUserDto> getUserFollowings(UUID targetUserId, UUID authUserId, FollowingFilter filter) {
        validateUserExists(targetUserId);

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(filter.getPageNo(), filter.getPageSize(), sort);
        return followRepository.findUserFollowings(targetUserId, authUserId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<AuthUserFollowDto> getAuthUserFollowings(UUID authUserId, FollowingFilter filter) {
        validateUserExists(authUserId);

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(filter.getPageNo(), filter.getPageSize(), sort);
        return followRepository.findAuthUserFollowings(authUserId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<AuthUserFollowDto> getAuthUserFollowers(UUID authUserId, FollowerFilter filter) {
        validateUserExists(authUserId);

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(filter.getPageNo(), filter.getPageSize(), sort);
        return followRepository.findAuthUserFollowers(authUserId, pageable);
    }

    @Transactional
    public FollowDto createFollow(UUID followerId, UUID followingId) {
        validateNotSelfFollow(followerId, followingId);
        validateUsersExists(followerId, followingId);
        validateNotAlreadyFollowing(followerId, followingId);

        var follower = entityManager.getReference(User.class, followerId);
        var following = entityManager.getReference(User.class, followingId);

        var follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        var saved = followRepository.save(follow);
        log.info("User {} followed {}", followerId, followingId);
        return FollowDto.builder()
                .followerId(followerId)
                .followingId(followingId)
                .createdAt(saved.getCreatedAt())
                .build();


    }

    @Transactional
    public void unfollowUser(UUID followerId, UUID followingId) {
        validateNotSelfFollow(followerId, followingId);
        validateUsersExists(followerId, followingId);
        validateNotFollowing(followerId, followingId);

        int rowsAffected = followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
        log.warn("Affected rows {} user {} after unfollowing {}", rowsAffected, followerId, followingId);
    }

    public Boolean isFollowing(UUID followerId, UUID followingId) {
        return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    private void validateUserExists(UUID userId) {
        if(!userRepository.existsById(userId)) {
            log.error("User not found with ID: {}", userId);
            throw new ResourceNotFoundException("User not found");
        }
    }

    private void validateUsersExists(UUID followerId, UUID followingId) {
        if(!userRepository.existsById(followerId)) {
            log.error("Follower User is not found with ID : {}", followerId);
            throw new ResourceNotFoundException("Follower is not found");
        }

        if(!userRepository.existsById(followingId)) {
            log.error("Following User is not found with ID : {}", followingId);
            throw new ResourceNotFoundException("Following is not found");
        }
    }

    private void validateNotSelfFollow(UUID followerId, UUID followingId) {
        if(followerId.equals(followingId)) {
            log.error("User cannot follow themselves");
            throw new IllegalArgumentException("User cannot follow themselves");
        }
    }

    private void validateNotAlreadyFollowing(UUID followerId, UUID followingId) {
        if(isFollowing(followerId, followingId)) {
            log.error("User {} already following {} ", followerId, followingId);
            throw new AlreadyFollowingUserException(String.format("User %s already following %s", followerId, followingId));
        }
     }

     private void validateNotFollowing(UUID followerId, UUID followingId) {
        if(!isFollowing(followerId, followingId)) {
            log.error("User {} is not following {}", followerId, followingId);
            throw new UserNotFollowedException(String.format("User %s is not following %s", followerId, followingId));
        }
     }

}
