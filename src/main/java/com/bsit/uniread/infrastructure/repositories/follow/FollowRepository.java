package com.bsit.uniread.infrastructure.repositories.follow;

import com.bsit.uniread.application.dto.response.follow.AuthUserFollowDto;
import com.bsit.uniread.application.dto.response.follow.FollowUserDto;
import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface FollowRepository extends JpaRepository<Follow, UUID>, CrudRepository<Follow, UUID> {

    Boolean existsByFollowerIdAndFollowingId(UUID followerId, UUID followingId);

    @EntityGraph(attributePaths = {"following", "follower"})
    Page<Follow> findByFollower(User user, Pageable pageable);

    @Query(value = """
    SELECT new com.bsit.uniread.application.dto.response.follow.FollowUserDto(
        up.id,
        up.username,
        up.firstName,
        up.lastName,
        CONCAT(up.firstName, ' ', up.lastName),
        up.gender,
        up.photoUrl,
        (SELECT COUNT(f1) > 0 FROM Follow f1 WHERE f1.following.id = u.id AND f1.follower.id = :authUserId),
        (SELECT COUNT(f2) > 0 FROM Follow f2 WHERE f2.follower.id = u.id AND f2.following.id = :authUserId),
        (SELECT COUNT(f3) > 0 FROM Follow f3 WHERE f3.following.id = u.id AND f3.follower.id = :authUserId)
        AND (SELECT COUNT(f4) > 0 FROM Follow f4 WHERE f4.follower.id = u.id AND f4.following.id = :authUserId)
    )
    FROM User u
    LEFT JOIN u.profile up
    WHERE u.id IN ( SELECT f.follower.id FROM Follow f.following.id = :targetUserId )
    """,
    countQuery = """
    SELECT COUNT(f) FROM Follow f WHERE f.following.id = :targetUserId
    """
    )
    Page<FollowUserDto> findUserFollowers(@Param("targetUserId") UUID targetUserId, @Param("authUserId") UUID authUserId, Pageable pageable);

    @Query(value = """
    SELECT new com.bsit.uniread.application.dto.response.follow.FollowUserDto(
        u.id,
        u.username,
        up.firstName,
        up.lastName,
        CONCAT(up.firstName, ' ', up.lastName),
        up.gender,
        up.photoUrl,
        (SELECT COUNT(f1) > 0 FROM Follow f1 WHERE f1.following.id = u.id AND f1.follower.id = :authUserId),
        (SELECT COUNT(f2) > 0 FROM Follow f2 WHERE f2.follower.id = u.id AND f2.following.id = :authUserId),
        (SELECT COUNT(f3) > 0 FROM Follow f3 WHERE f3.follower.id = :authUserId AND f3.following.id = u.id)
        AND (SELECT COUNT(f4) > 0 FROM Follow f4 WHERE f4.follower.id = u.id AND f4.following.id = :authUserId)
    )
    FROM User u
    LEFT JOIN u.profile up
    WHERE u.id IN ( SELECT f5.following.id FROM Follow f5 WHERE f5.follower.id = :targetUserId )
    """,
    countQuery = """
    SELECT COUNT(f) FROM Follow f WHERE f.follower.id = :targetUserId
    """
    )
    Page<FollowUserDto> findUserFollowings(@Param("targetUserId") UUID targetUserId, @Param("authUserId") UUID authUserId, Pageable pageable);

    @Query(value = """
    SELECT new com.bsit.uniread.application.dto.response.follow.AuthUserFollowDto(
        u.id,
        u.username,
        COALESCE(up.firstName, ''),
        COALESCE(up.lastName, ''),
        CONCAT(COALESCE(up.firstName, ''), ' ', COALESCE(up.lastName, '')),
        COALESCE(up.gender, com.bsit.uniread.domain.entities.user.Gender.OTHER),
        COALESCE(up.photoUrl, '')
    )
    FROM Follow f
    LEFT JOIN f.following u
    LEFT JOIN u.profile up
    WHERE f.follower.id = :authUserId
    """,
    countQuery = """
    SELECT COUNT(f.id) FROM Follow f WHERE f.follower.id = :authUserId
    """
    )
    Page<AuthUserFollowDto> findAuthUserFollowings(@Param("authUserId") UUID authUserId, Pageable pageable);

    @Query(value = """
    SELECT new com.bsit.uniread.application.dto.response.follow.AuthUserFollowDto(
        u.id,
        u.username,
        COALESCE(up.firstName, ''),
        COALESCE(up.lastName, ''),
        CONCAT(COALESCE(up.firstName, ''), ' ', COALESCE(up.lastName, '')),
        COALESCE(up.gender, com.bsit.uniread.domain.entities.user.Gender.OTHER),
        COALESCE(up.photoUrl, '')
    )
    FROM Follow f
    LEFT JOIN f.follower u
    LEFT JOIN u.profile up
    WHERE f.following.id = :authUserId
    """,
            countQuery = """
    SELECT COUNT(f.id) FROM Follow f WHERE f.following.id = :authUserId
    """
    )
    Page<AuthUserFollowDto> findAuthUserFollowers(@Param("authUserId") UUID authUserId, Pageable pageable);

    int deleteByFollowerIdAndFollowingId(UUID followerId, UUID followingId);

    @Query("""
        SELECT COUNT(f) = 2 
        FROM Follow f 
        WHERE (f.follower.id = :id1 AND f.following.id = :id2) 
           OR (f.follower.id = :id2 AND f.following.id = :id1)
    """)
    boolean existsAsFriends(@Param("id1") UUID id1, @Param("id2") UUID id2);

}
