package com.bsit.uniread.infrastructure.repositories;

import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface FollowRepository extends JpaRepository<Follow, UUID>, CrudRepository<Follow, UUID> {

    Boolean existsByFollowerAndFollowing(User requester, User following);

    Page<Follow> findByFollower(User user, Pageable pageable);
    Page<Follow> findByFollowing(User user, Pageable pageable);
    Page<Follow> findByFollowerAndFollowingFirstNameContainingIgnoreCaseOrFollowingLastNameContainingIgnoreCaseOrFollowingUsernameContainingIgnoreCase(User user, String firstName, String lastName, String username, Pageable pageable);
    Page<Follow> findByFollowingAndFollowerFirstNameContainingIgnoreCaseOrFollowerLastNameContainingIgnoreCaseOrFollowerUsernameContainingIgnoreCase(User user, String firstName, String lastName, String username, Pageable pageable);

    // Delete the following from follower
    Integer deleteByFollowingAndFollower(User followedUser, User userRequester);

}
