package com.bsit.uniread.infrastructure.repositories.user;

import com.bsit.uniread.application.dto.response.profile.ProfileDashboardDto;
import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository
        extends JpaRepository<User, UUID>, CrudRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Optional<User> findByGoogleUuid(String googleUuid);
    // Search the users based on given email
    Optional<User> findByEmail(String email);
    // Search the users based on given username
    Optional<User> findByUsernameContainingIgnoreCase(String username);
    /**
     * Get the users excluding list of user ids
     * @source https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
     * @return pagination of users
     */
    Page<User> findByIdNotIn(List<UUID> uuids, Specification<User> specification, Pageable pageable);
    @Query(
            value = """
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
                    FROM users u
                    WHERE u.id = :userId
            """
    )
    ProfileDashboardDto findByUserIdNative(@Param("userId") UUID userId);
}
