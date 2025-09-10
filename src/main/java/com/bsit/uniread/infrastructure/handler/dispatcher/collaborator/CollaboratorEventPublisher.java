package com.bsit.uniread.infrastructure.handler.dispatcher.collaborator;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.domain.events.collaborator.NewBookCollaboratorEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CollaboratorEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void approvedCollaboratorRequest(Book book, User user) {
        applicationEventPublisher.publishEvent(new NewBookCollaboratorEvent(this, book, user));
    }
}
