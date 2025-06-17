package com.bsit.uniread.infrastructure.security.authorizations.book;

import com.bsit.uniread.application.services.book.BookService;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component("bookPermission")
@RequiredArgsConstructor
public class BookPermission {

    private final BookService bookService;

    /**
     * Check if the given book id is author of current authenticated user
     * @param bookId
     * @param authentication
     * @return boolean
     */
    public boolean isAuthor(UUID bookId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Book book = bookService.getBookById(bookId);

        return Objects.equals(user, book.getUser());
    }
}
