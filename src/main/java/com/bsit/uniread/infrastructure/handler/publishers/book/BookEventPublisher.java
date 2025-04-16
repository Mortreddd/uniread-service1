package com.bsit.uniread.infrastructure.handler.publishers.book;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.events.book.NewPublishedBook;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void newPublishBook(Book book) {
        publisher.publishEvent(new NewPublishedBook(this, book));
    }
}
