package com.uniread.social.repositories;

import com.uniread.social.domain.entities.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface FollowRepository extends JpaRepository<Follow, UUID> {

    boolean existsByFollowerIdAndFollowingId(UUID followerId, UUID followingId);

    void deleteByFollowerIdAndFollowingId(UUID followerId, UUID followingId);

    @Query("""
        SELECT COUNT(f) = 2
        FROM Follow f
        WHERE (f.follower.id = :id1 AND f.following.id = :id2)
           OR (f.follower.id = :id2 AND f.following.id = :id1)
    """)
    boolean existsAsMutual(@Param("id1") UUID id1, @Param("id2") UUID id2);

    

}