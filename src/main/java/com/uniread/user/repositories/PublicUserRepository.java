package com.uniread.user.repositories;

import com.uniread.auth.domain.entities.User;
import com.uniread.user.domain.entities.Gender;
import com.uniread.user.dto.response.AuthorProfileDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PublicUserRepository extends
        JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    @Query(
        value = """
        SELECT
            new com.uniread.user.dto.response.AuthorProfileDetail(
                u1.id,
                u1.username,
                up1.firstName,
                up1.lastName,
                up1.displayName,
                up1.gender,
                up1.avatarPublicId,
                up1.coverPublicId,
                COALESCE(up1.followerCount, 0),
                COALESCE(up1.followingCount, 0),
                false,
                false,
                false,
                COALESCE(up1.publishedStoriesCount, 0)
            )
        FROM User u1
        LEFT JOIN u1.profile up1
        WHERE u1.bannedAt IS NULL
        AND u1.emailVerifiedAt IS NOT NULL
        AND u1.deletedAt IS NULL
        AND u1.id != :authUserId
        """,
        countQuery = """
        SELECT COALESCE(COUNT(u1.id), 0)
        FROM User u1
        WHERE u1.bannedAt IS NULL
        AND u1.emailVerifiedAt IS NOT NULL
        AND u1.deletedAt IS NULL
        AND u1.id != :authUserId
        """
    )
    Page<AuthorProfileDetail> findPublicUsers(@Param("authUserId") UUID authUserId, Pageable pageable);

//    @Query("""
//    SELECT
//            new com.uniread.user.dto.response.AuthorProfileDetail(
//                u1.id,
//                u1.username,
//                up1.firstName,
//                up1.lastName,
//                up1.displayName,
//                up1.gender,
//                up1.avatarPublicId,
//                up1.coverPublicId,
//                COALESCE(up1.followerCount, 0),
//                COALESCE(up1.followingCount, 0),
//                ,
//                false,
//                false,
//                COALESCE(up1.publishedStoriesCount, 0)
//            )
//        FROM User u1
//        LEFT JOIN u1.profile up1
//        WHERE u1.bannedAt IS NULL
//        AND u1.emailVerifiedAt IS NOT NULL
//        AND u1.deletedAt IS NULL
//        AND u1.username = :username
//    """)
//    Optional<AuthorProfileDetail> findPublicUserByUsername(@Param("username") String username);
}
