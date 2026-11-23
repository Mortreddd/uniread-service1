package com.uniread.user.specifications;


import com.uniread.auth.domain.entities.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public class PublicUserSpecification {

    public static Specification<User> hasBanned(Instant bannedAt) {
        return (root, query, builder) -> {
            if(bannedAt == null) return null;
            return builder.isNotNull(root.get("bannedAt"));
        };
    }

}
