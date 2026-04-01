package com.bsit.uniread.infrastructure.repositories.user;

import com.bsit.uniread.application.dto.response.profile.ProfileDashboardDto;
import com.bsit.uniread.application.dto.response.user.AuthorDetail;
import com.bsit.uniread.application.dto.response.user.CurrentUser;
import com.bsit.uniread.domain.entities.book.BookStatus;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import com.bsit.uniread.domain.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository
        extends JpaRepository<User, UUID>, CrudRepository<User, UUID>, JpaSpecificationExecutor<User> {

    @EntityGraph(attributePaths = {"profile"})
    Page<User> findAll(@NotNull Specification<User> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"profile"})
    Optional<User> findById(UUID userId);

    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);


    @Modifying
    @Query("UPDATE User SET username = :username WHERE id = :id")
    void updateUsername(@Param("username") String username, @Param("id") UUID id);

    Optional<User> findByEmail(String email);
    Optional<User> findByGoogleUuid(String googleUuid);
    // Search the users based on given email
    Optional<CustomUserDetails> findByEmailOrUsername(String email);
    // Search the users based on given username
    Optional<User> findByUsernameContainingIgnoreCase(String username);

    @Query("""
            SELECT com.bsit.uniread.domain.entities.user.CustomUserDetails(
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
            SELECT com.bsit.uniread.domain.entities.user.CustomUserDetails(
                u.id,
                profile.firstName,
                profile.lastName,
                CONCAT(profile.firstName, ' ', profile.lastName),
                u.username,
                profile.gender,
                u.email,
                profile.avatarUrl,
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
    SELECT new com.bsit.uniread.application.dto.response.user.AuthorDetail(
        u.id,
        up.username,
        up.firstName,
        up.lastName,
        CONCAT(up.firstName, ' ', up.lastName),
        up.gender,
        up.photoUrl,
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
    GROUP BY u.id, up.username, up.firstName, up.lastName, up.gender, up.photoUrl
    """,
    countQuery = """
    SELECT COUNT(u) FROM User u
    WHERE u.bannedAt IS NULL
      AND u.deletedAt IS NULL
      AND u.id <> :id
    """
    )
    Page<AuthorDetail> findAuthors(@Param("id") UUID id, @Param("bookStatus") BookStatus bookStatus, Pageable pageable);


    @Query(value =
            """
            SELECT new com.bsit.uniread.application.dto.response.user.AuthorDetail(
                    u.id,
                    u.username,
                    u.firstName,
                    u.lastName,
                    CONCAT(u.firstName, ' ', u.lastName),
                    u.gender,
                    u.photoUrl,
            
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
            WHERE u.bannedAt IS NULL AND u.deletedAt IS NULL AND u.id = :id
            """
    )
    Optional<AuthorDetail> findByAuthorId(@Param("id") UUID userId, @Param("bookStatus") BookStatus bookStatus);

    @Query(value = """
    SELECT
    new com.bsit.uniread.application.dto.response.profile.ProfileDashboardDto(
      (SELECT COUNT(*) FROM Book b WHERE b.user.id = :userId),
      (SELECT COUNT(*) FROM Book b WHERE b.user.id = :userId AND b.status = 'PUBLISHED'),
      (SELECT COUNT(*) FROM Book b WHERE b.user.id = :userId AND b.status = 'DRAFT'),
      (SELECT COALESCE(SUM(bc.rating), 0)
       FROM Book b
       LEFT JOIN BookComment bc ON bc.book.id = b.id
       WHERE b.user.id = :userId),
      (SELECT COALESCE(COUNT(bl.id), 0)
       FROM Book b
       LEFT JOIN BookLike bl ON bl.book.id = b.id
       WHERE b.user.id = :userId),
      (SELECT COALESCE(SUM(c.readCount), 0)
       FROM Book b
       LEFT JOIN Chapter c ON c.book.id = b.id
       WHERE b.user.id = :userId),
      (SELECT COUNT(*) FROM Follow f WHERE f.following.id = :userId),
      (SELECT COUNT(*) FROM Follow f WHERE f.follower.id = :userId)
     )
    FROM User u
    WHERE u.id = :userId
    """
    )
    ProfileDashboardDto findUserDashboard(@Param("userId") UUID userId);

    @Query("SELECT COUNT(u) = :size FROM User u WHERE u.id IN :userIds AND u.bannedAt IS NULL")
    boolean existsUsers(@Param("userIds") List<UUID> userIds, @Param("size") long size);
}
