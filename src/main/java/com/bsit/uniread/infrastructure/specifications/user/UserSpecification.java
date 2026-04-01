package com.bsit.uniread.infrastructure.specifications.user;

import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import com.bsit.uniread.domain.entities.user.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class UserSpecification {

    public static Specification<User> hasBanned(Instant bannedAt) {
        return (root, query, builder) -> {
            if(bannedAt == null) return null;
            return builder.isNotNull(root.get("bannedAt"));
        };
    }

    public static Specification<User> hasQuery(String search) {
        return (root, query, builder) -> {
            if(search == null || search.isBlank()) return null;
            String pattern = "%" + search.toLowerCase() + "%";
            return builder.or(
                    builder.like(builder.lower(root.get("profile").get("firstName")), pattern),
                    builder.like(builder.lower(root.get("profile").get("lastName")), pattern),
                    builder.like(builder.lower(root.get("username")), pattern)
            );
        };
    }

    public static Specification<User> hasEmailVerified(Boolean hasEmailVerified) {
        return (root, query, builder) -> {
            if(Boolean.FALSE.equals(hasEmailVerified)) return null;
            return builder.isNotNull(root.get("emailVerifiedAt"));
        };
    }

    public static Specification<User> hasDeleted(Instant deletedAt) {
        return (root, query, builder) -> {
            if(deletedAt == null) return null;
            return builder.isNotNull(root.get("deletedAt"));
        };
    }

    public static Specification<User> hasNotIn(List<UUID> ids) {
        return (root, query, builder) -> {
            if(ids == null || ids.isEmpty()) return builder.conjunction();

            return builder.not(root.get("id").in(ids));
        };
    }

    public static Specification<User> hasAuthUser(UUID authUserId) {
        return (root, query, builder) -> {
            if(authUserId == null) return builder.conjunction();

            return builder.notEqual(root.get("id"), authUserId);
        };
    }


}
