package com.bsit.uniread.domain.events.follow;

import com.bsit.uniread.domain.entities.Follow;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserUnfollowEvent extends ApplicationEvent {

    private final Follow follow;

    public UserUnfollowEvent(Object source, Follow follow) {
        super(source);
        this.follow = follow;
    }
}
