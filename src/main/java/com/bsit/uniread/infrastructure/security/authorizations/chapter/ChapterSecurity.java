package com.bsit.uniread.infrastructure.security.authorizations.chapter;

import com.bsit.uniread.application.services.book.BookService;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component("chapterSecurity")
@RequiredArgsConstructor
public class ChapterSecurity {

    private final BookService bookService;

    public boolean isAuthor(UUID bookId, Authentication authentication) {
        User user =  (User) authentication.getPrincipal();
        Book book = bookService.getBookById(bookId);
        return Objects.equals(book.getUser(), user);
    }
}
