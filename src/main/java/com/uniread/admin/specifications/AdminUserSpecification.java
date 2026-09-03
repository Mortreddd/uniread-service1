package com.uniread.admin.specifications;

import com.uniread.auth.domain.entities.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.UUID;

public class AdminUserSpecification {

    public static Specification<User> hasQuery(String search) {
        return (root, query, builder) -> {
            if (search == null || search.isBlank()) {
                return builder.conjunction();
            }
            String pattern = "%" + search.toLowerCase() + "%";
            return builder.or(
                    builder.like(builder.lower(root.get("profile").get("firstName")), pattern),
                    builder.like(builder.lower(root.get("profile").get("lastName")), pattern),
                    builder.like(builder.lower(root.get("username")), pattern)
            );
        };
    }

    public static Specification<User> hasExcludedUser(UUID userId) {
        return (root, query, builder) -> {
            if (userId == null) {
                return builder.conjunction();
            }
            return builder.notEqual(root.get("id"), userId);
        };
    }

    public static Specification<User> hasRegisteredAt(Instant timestamp) {
        return (root, query, builder) -> {
            if (timestamp == null) {
                return builder.conjunction();
            }
            return builder.greaterThanOrEqualTo(root.get("createdAt"), timestamp);
        };
    }

    public static Specification<User> hasEmailVerified(Boolean hasEmailVerified) {
        return (root, query, builder) -> {
            if (hasEmailVerified == null || !hasEmailVerified) {
                return builder.conjunction();
            }
            return builder.isNotNull(root.get("emailVerifiedAt"));
        };
    }
}