package com.bsit.uniread.domain.events.book;


import com.bsit.uniread.domain.entities.book.Book;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NewBookEvent extends ApplicationEvent {

    private final Book book;
    public NewBookEvent(Object source, Book book) {
        super(source);
        this.book = book;
    }

}
