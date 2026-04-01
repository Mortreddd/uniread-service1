package com.bsit.uniread.infrastructure.specifications.notification;

import com.bsit.uniread.domain.entities.notification.Notification;
import org.springframework.data.jpa.domain.Specification;

public class NotificationSpecification {

    public static Specification<Notification> hasQuery(String search) {
        return (root, query, builder) -> builder.or(
                builder.like(builder.lower(root.get("title")), search),
                builder.like(builder.lower(root.get("user").get("profile").get("firstName")), search),
                builder.like(builder.lower(root.get("user").get("profile").get("lastName")), search),
                builder.like(builder.lower(root.get("user").get("profile").get("username")), search)
        );
    }
}
