package com.uniread.user.repositories;

import com.uniread.auth.domain.entities.User;
import com.uniread.user.domain.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID>{

    Optional<UserProfile> findByUserId(UUID userId);

    UUID user(User user);

    @Modifying
    @Query("UPDATE UserProfile SET followingCount = COALESCE(followingCount, 0) - 1 WHERE user.id = :followerId")
    void decreaseFollowing(@Param("followerId") UUID followerId);

    @Modifying
    @Query("UPDATE UserProfile SET followerCount = COALESCE(followerCount, 0) - 1 WHERE user.id = :followingId")
    void decreaseFollower(@Param("followingId") UUID followingId);

    @Modifying
    @Query("UPDATE UserProfile SET followingCount = COALESCE(followingCount, 0) + 1 WHERE user.id = :followerId")
    void increaseFollowing(@Param("followerId") UUID followerId);

    @Modifying
    @Query("UPDATE UserProfile SET followerCount = COALESCE(followerCount, 0) + 1 WHERE user.id = :followingId")
    void increaseFollower(@Param("followingId") UUID followingId);


}
