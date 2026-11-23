package com.uniread.social.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class NewFollowerEvent extends ApplicationEvent {

    private final UUID followingId;
    private final String username;
    private final String displayName;

    public NewFollowerEvent(
            UUID followingId,
            String username,
            String displayName
    ) {
        super(followingId);
        this.followingId = followingId;
        this.username = username;
        this.displayName = displayName;
    }
}
