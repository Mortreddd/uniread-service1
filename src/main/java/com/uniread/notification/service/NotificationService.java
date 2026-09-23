package com.uniread.notification.service;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.notification.domain.entities.NotificationMessage;
import com.uniread.notification.dto.response.TotalUnreadNotification;
import com.uniread.notification.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;
    private final List<NotificationSender> senders;

    public TotalUnreadNotification getUnreadCount(CustomUserDetails userDetails) {
        var unreadCount = repository.countUnread(userDetails.getId());
        return new TotalUnreadNotification(unreadCount);
    }

    @Transactional
    public void notify(NotificationMessage message) {

        for(var sender : senders) {
            if(message.getChannels().contains(sender.getChannel())) {
                sender.send(message);
            }
        }

    }

}
