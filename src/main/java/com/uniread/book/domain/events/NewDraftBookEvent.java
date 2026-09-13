package com.uniread.book.domain.events;

import com.uniread.book.domain.entities.Book;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class NewDraftBookEvent extends ApplicationEvent {

    private final Book book;

    public NewDraftBookEvent(UUID bookId, Book book) {
        super(bookId);
        this.book = book;
    }
}
