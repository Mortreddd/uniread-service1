package com.bsit.uniread.infrastructure.repositories.user;

import com.bsit.uniread.domain.entities.user.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BlockRepository extends JpaRepository<Block, UUID> {

    @Query("""
    SELECT COUNT(b) > 0 FROM Block b
    WHERE (b.blocker.id = :user1 AND b.blocked.id = :user2) 
    OR (b.blocker.id = :user2 AND b.blocked.id = :user1))
    """)
    Boolean existsAnyBlocked(@Param("user1") UUID user1, @Param("user2") UUID user2);
}
