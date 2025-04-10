package com.bsit.uniread.infrastructure.handler.listeners.book;

import com.bsit.uniread.application.services.NotificationService;
import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.domain.events.book.NewBookEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookCreatedListener implements ApplicationListener<NewBookEvent> {

    private final NotificationService notificationService;

    @Override
    public void onApplicationEvent(NewBookEvent event) {
        String bookTitle = event.getBook().getTitle();
        User author = event.getBook().getUser();
        String notificationTitle = "New Book Released";
        String notificationDescription = String.format("%s created a new book %s", author.getUsername(), bookTitle);
        List<User> followers = author.getFollowers()
                .stream()
                .map(Follow::getFollower)
                .toList();

        notificationService.notifyUsers(notificationTitle, notificationDescription, followers);
    }
}
