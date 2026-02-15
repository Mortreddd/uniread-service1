package com.bsit.uniread.domain.events.collaborator;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.user.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NewBookCollaboratorEvent extends ApplicationEvent {

    private final Book book;
    private final User user;

    public NewBookCollaboratorEvent(Object source, Book book, User user) {
        super(source);
        this.book = book;
        this.user = user;
    }

}
