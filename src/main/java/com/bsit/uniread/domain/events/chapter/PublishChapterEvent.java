package com.bsit.uniread.domain.events.chapter;

import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.chapter.Chapter;
import com.bsit.uniread.domain.entities.user.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class PublishChapterEvent extends ApplicationEvent {

    private final Chapter chapter;
    private final Book book;
    private final User author;
    private final List<User> followers;

    public PublishChapterEvent(Object source, Chapter chapter) {
        super(source);
        this.chapter = chapter;
        this.book = chapter.getBook();
        this.author = book.getUser();
        this.followers = author.getFollowers()
                .stream()
                .map(Follow::getFollower)
                .toList();
    }
}
