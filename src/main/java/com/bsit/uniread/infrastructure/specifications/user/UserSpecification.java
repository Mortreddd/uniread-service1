package com.bsit.uniread.infrastructure.specifications.user;

import com.bsit.uniread.domain.entities.user.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> hasBanned(String bannedAt) {
        return (root, query, builder) -> {
            if(bannedAt == null || bannedAt.isBlank()) return null;

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

}
