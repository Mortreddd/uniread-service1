package com.bsit.uniread.infrastructure.handler.listeners.chapter;

import com.bsit.uniread.application.dto.response.notification.NotificationDto;
import com.bsit.uniread.application.services.FollowService;
import com.bsit.uniread.application.services.notification.NotificationDispatcher;
import com.bsit.uniread.application.services.notification.NotificationService;
import com.bsit.uniread.application.services.notification.PrivateNotifier;
import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.notification.Notification;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.chapter.Chapter;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.domain.events.chapter.PublishChapterEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PublishingChapterListener {

    private final NotificationService notificationService;
    private final FollowService followService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handlePublishingChapter(PublishChapterEvent event) {
        Chapter chapter = event.getChapter();
        Book book = event.getBook();
        User author = event.getAuthor();
        List<User> followers = followService.getFollowersByUser(author)
                .stream()
                .map(Follow::getFollower)
                .toList();

        String title = String.format("%s has new chapter", book.getTitle());
        String description = String.format("%s released a new chapter of %s", author.getUsername(), chapter.getTitle());

        List<Notification> notifications = notificationService.saveNotifications(notificationService.newNotifications(followers, title, description));

        List<NotificationDto> notifs = notifications
                .stream()
                .map(NotificationDto::new)
                .toList();


        NotificationDispatcher dispatcher = new NotificationDispatcher();
        dispatcher.addNotifier(new PrivateNotifier(followers, notifs, simpMessagingTemplate));
        dispatcher.dispatch();
    }
}
