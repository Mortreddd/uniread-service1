package com.bsit.uniread.domain.events.follow;

import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.user.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class NewFollowerEvent extends ApplicationEvent {

    private final User receiver;
    private final User sender;

    public NewFollowerEvent(Object source, Follow follow) {
        super(source);
        this.sender = follow.getFollower();
        this.receiver = follow.getFollowing();
    }

}
