package com.bsit.uniread.infrastructure.handler.listeners.follow;

import com.bsit.uniread.application.dto.response.notification.NotificationDto;
import com.bsit.uniread.application.services.FollowService;
import com.bsit.uniread.application.services.NotificationService;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.domain.events.follow.NewFollowerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * An Event Listener for every new follow transactions created
 */
@Component
@RequiredArgsConstructor
public class NewFollowerListener implements ApplicationListener<NewFollowerEvent> {

    private final NotificationService notificationService;
    private final FollowService followService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Override
    public void onApplicationEvent(NewFollowerEvent event) {
        // The sender of the notification
        User sender = event.getSender();
        User receiver = event.getReceiver();

        String title = "New Follower";
        String description = String.format("%s follows you.", sender.getUsername());

        if(followService.isFollowingUser(receiver, sender)) {
            description = String.format("%s followed you back", sender.getUsername());
        }
        // Create notification and send notification email
        NotificationDto newNotification = new NotificationDto(notificationService.createNotification(receiver, title, description));
        simpMessagingTemplate.convertAndSendToUser(receiver.getId().toString(), "/queue/notifications", newNotification);
    }
}
