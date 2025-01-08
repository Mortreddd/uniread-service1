package com.bsit.uniread.domain.events;

import com.bsit.uniread.domain.entities.book.Book;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Every book created, it will be sent to all followers, create a notification
 * This will suppose to work asynchronously
 * @params Object source, Book newBook
 */

@Getter
public class CreatedBookEvent extends ApplicationEvent {

    private final Book createdBook;

    public CreatedBookEvent(Object source, Book newBook) {
        super(source);
        this.createdBook = newBook;
    }

}
