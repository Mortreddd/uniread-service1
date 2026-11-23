package com.uniread.user.repositories;

import com.uniread.social.domain.entities.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserFollowRepository extends JpaRepository<Follow, UUID> {

    @Query("""
    SELECT f FROM Follow f
    WHERE (f.follower.id IN :authorIds AND f.following.id = :currentUserId)
       OR (f.following.id IN :authorIds AND f.follower.id = :currentUserId)
    """)
    List<Follow> findEdgesBetween(UUID currentUserId, Collection<UUID> authorIds);

}
