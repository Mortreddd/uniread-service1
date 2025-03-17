package com.bsit.uniread.domain.events.follow;

import com.bsit.uniread.domain.entities.Follow;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class NewFollowerEvent extends ApplicationEvent {

    private final Follow follow;

    public NewFollowerEvent(Object source, Follow follow) {
        super(source);
        this.follow = follow;
    }

}
