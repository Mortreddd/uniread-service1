package com.bsit.uniread.infrastructure.handler.listeners;

import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.domain.events.CreatedBookEvent;
import com.bsit.uniread.application.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AppriseFollowerForBookCreationListener implements ApplicationListener<CreatedBookEvent> {

    private final NotificationService notificationService;

    @Override
    public void onApplicationEvent(CreatedBookEvent event) {
        User author = event.getCreatedBook().getUser();
        List<User> followers = author.getFollowers()
                .stream()
                .map(Follow::getFollower)
                .toList();

        String notificationTitle = String.format("%s created a new book", author.getUsername());
        String notificationDescription = String
                .format("%s published a new book entitled %s", author.getUsername(), event.getCreatedBook().getTitle());

        notificationService.notifyUsers(notificationTitle, notificationDescription, followers);
    }
}
