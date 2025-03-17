package com.bsit.uniread.infrastructure.handler.publishers.follower;

import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.events.follow.NewFollowerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishNewFollower(Follow follow) {
        applicationEventPublisher.publishEvent(new NewFollowerEvent(this, follow));
    }

    public void publishUserUnfollow(Follow follow) {

    }
}
