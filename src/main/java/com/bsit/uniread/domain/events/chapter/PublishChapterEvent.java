package com.bsit.uniread.domain.events.chapter;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.chapter.Chapter;
import com.bsit.uniread.domain.entities.user.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PublishChapterEvent extends ApplicationEvent {

    private final Chapter chapter;
    private final Book book;
    private final User author;

    public PublishChapterEvent(Object source, Chapter chapter) {
        super(source);
        this.chapter = chapter;
        Book book = chapter.getBook();
        this.book = book;
        this.author = book.getUser();
    }
}
