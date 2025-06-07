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

    /**
     * Checks if the two user is mutual
     * @param requester
     * @param following
     * @param mutualFollowing
     * @param mutualRequester
     * @return boolean
     */
    Boolean existsByFollowerAndFollowingAndFollowingAndFollower(User requester, User following, User mutualFollowing, User mutualRequester);
    Page<Follow> findByFollower(User user, Pageable pageable);
    Page<Follow> findByFollowing(User user, Pageable pageable);

    Page<Follow> findByFollowingOrFollower(User following, User follower, Pageable pageable);

    Page<Follow> findByFollowingOrFollowerOrFollowerFirstNameContainingIgnoreCaseOrFollowerLastNameContainingIgnoreCaseOrFollowerUsernameContainingIgnoreCaseOrFollowingFirstNameContainingIgnoreCaseOrFollowingLastNameContainingIgnoreCaseOrFollowingUsernameContainingIgnoreCase(User follower, User following, String followerFirstName, String followerLastName, String followerUsername, String followingFirstName, String followingLastName, String followingUsername, Pageable pageable);
    /**
     * Get the followings and search for the followings
     * @param user
     * @param firstName
     * @param lastName
     * @param username
     * @param pageable
     * @return page of followings
     */
    Page<Follow> findByFollowerAndFollowingFirstNameContainingIgnoreCaseOrFollowingLastNameContainingIgnoreCaseOrFollowingUsernameContainingIgnoreCase(User user, String firstName, String lastName, String username, Pageable pageable);

    /**
     * Get the followers and search the followers
     * @param user
     * @param firstName
     * @param lastName
     * @param username
     * @param pageable
     * @return page of followers
     */
    Page<Follow> findByFollowingAndFollowerFirstNameContainingIgnoreCaseOrFollowerLastNameContainingIgnoreCaseOrFollowerUsernameContainingIgnoreCase(User user, String firstName, String lastName, String username, Pageable pageable);

    // Create an unfollow transaction
    Integer deleteByFollowerAndFollowing(User currentUser, User unfollowedUser);

}
