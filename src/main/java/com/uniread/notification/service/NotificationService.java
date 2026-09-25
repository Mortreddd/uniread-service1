package com.uniread.notification.service;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.notification.domain.entities.NotificationMessage;
import com.uniread.notification.dto.request.NotificationFilter;
import com.uniread.notification.dto.response.NotificationDto;
import com.uniread.notification.dto.response.TotalUnreadNotification;
import com.uniread.notification.mappers.NotificationMapper;
import com.uniread.notification.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationValidator validator;
    private final NotificationMapper mapper;
    private final NotificationRepository repository;
    private final List<NotificationSender> senders;

    public Page<NotificationDto> getUserNotifications(
            NotificationFilter filter,
            CustomUserDetails userDetails
    ) {

        var pageable = PageRequest.of(
                filter.getPageNo(),
                filter.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return repository.findByUserId(userDetails.getId(), pageable)
                .map(mapper::toDto);
    }

    public TotalUnreadNotification getUnreadCount(CustomUserDetails userDetails) {
        var unreadCount = repository.countUnread(userDetails.getId());
        return new TotalUnreadNotification(unreadCount);
    }

    @Transactional
    public void markReadNotification(CustomUserDetails userDetails) {

        repository.markReadNotifications(userDetails.getId());
    }

    @Transactional
    public void updatedClickedNotification(UUID notificationId, CustomUserDetails userDetails) {
        validator.isOwner(notificationId, userDetails.getId());
        repository.updatedClickedNotification(notificationId);
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
