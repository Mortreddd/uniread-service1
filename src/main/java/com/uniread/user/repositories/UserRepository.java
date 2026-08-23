package com.uniread.user.repositories;

import com.uniread.auth.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository
        extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    @EntityGraph(attributePaths = {"profile"})
    @Query("SELECT u FROM User u")
    Page<User> findAllWithProfile(Specification<User> spec, Pageable pageable);

    @Query("""
    SELECT u
    FROM User u
    JOIN u.profile p
        WHERE (
            u.username ILIKE CONCAT(:query, '%')
            OR p.displayName ILIKE CONCAT(:query, '%')
            OR p.firstName ILIKE CONCAT(:query, '%')
            OR p.lastName ILIKE CONCAT(:query, '%')
        )
        AND u.id <> :authUserId
        AND u.bannedAt IS NULL
        AND u.deletedAt IS NULL
        AND u.emailVerifiedAt IS NOT NULL
    """)
    Page<User> findPublicProfiles(@Param("authUserId") UUID userId, @Param("query") String query, Pageable pageable);

    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);

    @Modifying
    @Query("UPDATE User SET username = :username WHERE id = :id")
    void updateUsername(@Param("username") String username, @Param("id") UUID id);

    @Modifying
    @Query("UPDATE User SET email = :email, emailVerifiedAt = NULL WHERE id = :userId")
    void updateEmail(@Param("userId") UUID userId, @Param("email") String email);

    Optional<User> findByEmail(String email);
    Optional<User> findByEmailOrUsername(String email, String username);

    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    @Query("SELECT u FROM User u WHERE u.id = :userId")
    Optional<User> findCurrentUserDetailsById(@Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE User SET emailVerifiedAt = :verifiedAt WHERE id = :userId")
    void markUserAsVerified(@Param("userId") UUID userId, @Param("verifiedAt") Instant verifiedAt);
}
