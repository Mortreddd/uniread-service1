package com.bsit.uniread.infrastructure.specifications.user;

import com.bsit.uniread.domain.entities.user.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public class UserSpecification {

    public static Specification<User> hasBanned(String bannedAt) {
        return (root, query, builder) -> {
            if(bannedAt == null || bannedAt.isBlank()) return builder.isNull(root.get("bannedAt"));

            return builder.isNotNull(root.get("bannedAt"));
        };
    }

    public static Specification<User> hasQuery(String search) {
        return (root, query, builder) -> {
            if(search == null || search.isBlank()) return null;
            return builder.or(
                    builder.like(builder.lower(root.get("firstName")), search),
                    builder.like(builder.lower(root.get("lastName")), search),
                    builder.like(builder.lower(root.get("username")), search)
            );
        };
    }

    public static Specification<User> hasEmailVerified(String emailVerifiedAt) {
        return (root, query, builder) -> {
            if(emailVerifiedAt == null || emailVerifiedAt.isBlank()) return builder.isNull(root.get("emailVerifiedAt"));

            return builder.isNotNull(root.get("emailVerifiedAt"));
        };
    }

    public static Specification<User> hasDeleted(String deletedAt) {
        return (root, query, builder) -> {
            if(deletedAt == null || deletedAt.isBlank()) return builder.isNull(root.get("deletedAt"));

            return builder.isNotNull(root.get("deletedAt"));
        };
    }

    public static Specification<User> hasNotIn(List<UUID> ids) {
        return (root, query, builder) -> {
            if(ids == null || ids.isEmpty()) return null;

            return builder.not(root.get("id").in(ids));
        };
    }


}
