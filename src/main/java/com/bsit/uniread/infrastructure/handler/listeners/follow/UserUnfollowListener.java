package com.bsit.uniread.infrastructure.handler.listeners.follow;

import com.bsit.uniread.application.services.FollowService;
import com.bsit.uniread.application.services.NotificationService;
import com.bsit.uniread.domain.events.follow.UserUnfollowEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserUnfollowListener implements ApplicationListener<UserUnfollowEvent> {

    private final FollowService followService;
    private final NotificationService notificationService;

    @Override
    public void onApplicationEvent(UserUnfollowEvent event) {

    }
}
