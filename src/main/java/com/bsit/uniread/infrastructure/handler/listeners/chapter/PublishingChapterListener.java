package com.bsit.uniread.infrastructure.handler.listeners.chapter;

import com.bsit.uniread.application.services.NotificationService;
import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.Notification;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.chapter.Chapter;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.domain.events.chapter.PublishChapterEvent;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PublishingChapterListener implements ApplicationListener<PublishChapterEvent> {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void onApplicationEvent(PublishChapterEvent event) {
        Chapter chapter = event.getChapter();
        Book book = event.getBook();
        User author = event.getAuthor();

        List<Follow> followers = author.getFollowers();

        String title = String.format("%s has new chapter", book.getTitle());
        String description = String.format("%s released a new chapter of %s", author.getUsername(), chapter.getTitle());

        List<Notification> notifications = followers
                .stream()
                .map((f) -> Notification.builder()
                        .isRead(false)
                        .user(f.getFollower())
                        .title(title)
                        .description(description)
                        .createdAt(DateUtil.now())
                        .build())
                .toList();

        notificationService.saveNotifications(notifications);
        notifications.forEach((notification) -> {
            simpMessagingTemplate.convertAndSendToUser(
                    notification
                            .getUser()
                            .getId()
                            .toString(),
                    "/topic/notifications",
                    notification
            );
        });
    }
}
