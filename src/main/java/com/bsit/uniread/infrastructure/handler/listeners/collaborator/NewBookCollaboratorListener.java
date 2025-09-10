package com.bsit.uniread.infrastructure.handler.listeners.collaborator;

import com.bsit.uniread.application.dto.response.notification.NotificationDto;
import com.bsit.uniread.application.services.collaborator.CollaboratorService;
import com.bsit.uniread.application.services.notification.NotificationService;
import com.bsit.uniread.domain.entities.collaborator.Collaborator;
import com.bsit.uniread.domain.entities.collaborator.CollaboratorPermission;
import com.bsit.uniread.domain.entities.notification.Notification;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.domain.events.collaborator.NewBookCollaboratorEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NewBookCollaboratorListener {

    private final CollaboratorService collaboratorService;
    private final NotificationService notificationService;

    @EventListener
    public void newBookCollaborator(NewBookCollaboratorEvent event) {
        // Default collaborator and it's permission
        Collaborator collaborator = Collaborator
                .builder()
                .book(event.getBook())
                .user(event.getUser())
                .permissions(List.of(CollaboratorPermission.EDIT_CHAPTER))
                .build();

        collaboratorService.save(collaborator);

        List<User> collaborators = collaboratorService.getBookCollaboratorsById(event.getBook().getId())
                .stream()
                .filter(u -> u.getId().equals(event.getUser().getId()))
                .map(Collaborator::getUser)
                .toList();

        String title = "New collaborator added";
        String description = String.format("%s has been added as collaborator", event.getUser().getFullName());
        List<Notification> notifications = notificationService.newNotifications(collaborators, title, description)
                .stream()
                .toList();

        notificationService.saveNotifications(notifications);
    }
}
