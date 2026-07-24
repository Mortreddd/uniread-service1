package com.uniread.user.repositories;

import com.uniread.user.dto.response.UserDetail;
import com.uniread.user.dto.response.CurrentUser;
import com.uniread.book.domain.entities.BookStatus;
import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.user.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository
        extends JpaRepository<User, UUID>, CrudRepository<User, UUID>, JpaSpecificationExecutor<User> {

    @EntityGraph(attributePaths = {"profile"})
    Page<User> findAll(Specification<User> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"profile"})
    Optional<User> findById(UUID userId);

    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);


    @Modifying
    @Query("UPDATE User SET username = :username WHERE id = :id")
    void updateUsername(@Param("username") String username, @Param("id") UUID id);

    @Modifying
    @Query("UPDATE User SET email = :email, emailVerifiedAt = NULL WHERE id = :userId")
    void updateEmail(@Param("userId") UUID userId, @Param("email") String email);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByGoogleUuid(String googleUuid);
    // Search the users based on given email
    Optional<CustomUserDetails> findByEmailOrUsername(String email, String username);
    // Search the users based on given username
    Boolean existsByUsernameContainingIgnoreCase(String username);

    @Query("""
            SELECT com.uniread.auth.domain.entities.CustomUserDetails(
                u.id,
                u.email,
                u.password,
                u.role,
                u.username,
                u.emailVerifiedAt,
                u.createdAt,
                u.updatedAt,
                u.bannedAt,
                u.unbannedAt,
                u.deletedAt
            )
            FROM User u
            WHERE u.id = :userId
            """
    )
    Optional<CustomUserDetails> findCurrentUserDetailsById(@Param("userId") UUID userId);

    @Query("""
            SELECT com.uniread.auth.domain.entities.CustomUserDetails(
                u.id,
                profile.firstName,
                profile.lastName,
                CONCAT(profile.firstName, ' ', profile.lastName),
                u.username,
                profile.gender,
                u.email,
                profile.avatarPhoto,
                u.role,
                u.emailVerifiedAt,
                u.createdAt,
                u.updatedAt,
                u.bannedAt,
                u.unbannedAt,
                u.deletedAt
            )
            FROM User u
            LEFT JOIN UserProfile profile ON profile.user.id = u.id
            WHERE u.id = :userId
            """
    )
    Optional<CurrentUser> findCurrentUserById(@Param("userId") UUID userId);

    @Query(
    value = """
    SELECT new com.uniread.user.dto.response.UserDetail(
        u.id,
        u.username,
        up.firstName,
        up.lastName,
        CONCAT(up.firstName, ' ', up.lastName),
        up.gender,
        up.avatarPhoto,
        COUNT(DISTINCT f_all_ers),
        COUNT(DISTINCT f_all_ing),
        COUNT(DISTINCT b),
        (COUNT(DISTINCT f_me_fol) > 0),
        (COUNT(DISTINCT f_me_er) > 0),
        (COUNT(DISTINCT f_me_fol) > 0 AND COUNT(DISTINCT f_me_er) > 0)
    )
    FROM User u
    LEFT JOIN u.profile up
    LEFT JOIN Follow f_all_ers ON f_all_ers.following = u
    LEFT JOIN Follow f_all_ing ON f_all_ing.follower = u
    LEFT JOIN Book b ON b.user = u AND b.status = :bookStatus
    LEFT JOIN Follow f_me_fol ON f_me_fol.following = u AND f_me_fol.follower.id = :id
    LEFT JOIN Follow f_me_er ON f_me_er.follower = u AND f_me_er.following.id = :id
    WHERE u.id <> :id
      AND u.bannedAt IS NULL
      AND u.deletedAt IS NULL
    GROUP BY u.id, u.username, up.firstName, up.lastName, up.gender, up.avatarPhoto
    """,
    countQuery = """
    SELECT COUNT(u) FROM User u
    WHERE u.bannedAt IS NULL
      AND u.deletedAt IS NULL
      AND u.id <> :id
    """
    )
    Page<UserDetail> findUsersDetail(@Param("id") UUID id, @Param("bookStatus") BookStatus bookStatus, Pageable pageable);


    @Query(value =
            """
            SELECT new com.uniread.user.dto.response.UserDetail(
                    u.id,
                    u.username,
                    profile.firstName,
                    profile.lastName,
                    CONCAT(profile.firstName, ' ', profile.lastName),
                    profile.gender,
                    profile.avatarPhoto,
            
                    (SELECT COUNT(f)
                     FROM Follow f
                     WHERE f.following = u),
            
                    (SELECT COUNT(f)
                     FROM Follow f
                     WHERE f.follower = u),
            
                    (SELECT COUNT(b)
                     FROM Book b
                     WHERE b.user = u AND b.status = :bookStatus),
            
                    EXISTS (
                        SELECT 1 FROM Follow f
                        WHERE f.follower.id = :id AND f.following = u
                    ),
            
                    EXISTS (
                        SELECT 1 FROM Follow f
                        WHERE f.follower = u AND f.following.id = :id
                    ),
            
                    CASE
                        WHEN
                            EXISTS (
                                SELECT 1 FROM Follow f
                                WHERE f.follower.id = :id AND f.following = u
                            )
                        AND
                            EXISTS (
                                SELECT 1 FROM Follow f
                                WHERE f.follower = u AND f.following.id = :id
                            )
                        THEN true
                        ELSE false
                    END
                )
            FROM User u
            LEFT JOIN u.profile profile
            WHERE u.bannedAt IS NULL AND u.deletedAt IS NULL AND u.id = :id
            """
    )
    Optional<UserDetail> findUserDetailById(@Param("id") UUID userId, @Param("bookStatus") BookStatus bookStatus);

    @Query("SELECT COUNT(u) = :size FROM User u WHERE u.id IN :userIds AND u.bannedAt IS NULL")
    boolean existsUsers(@Param("userIds") List<UUID> userIds, @Param("size") long size);

    @Modifying
    @Query("UPDATE User SET emailVerifiedAt = :verifiedAt WHERE id = :userId")
    void markUserAsVerified(@Param("userId") UUID userId, @Param("verifiedAt") Instant verifiedAt);
}
