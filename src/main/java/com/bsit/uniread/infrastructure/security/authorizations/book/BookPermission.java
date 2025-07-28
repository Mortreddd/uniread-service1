package com.bsit.uniread.infrastructure.security.authorizations.book;

import com.bsit.uniread.application.services.book.BookService;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component("bookPermission")
@RequiredArgsConstructor
public class BookPermission {

    private final UserService userService;
    private final BookService bookService;

    /**
     * Check if the given book id is author of current authenticated user
     * @param bookId
     * @param userDetails
     * @return boolean
     */
    public boolean isAuthor(UUID bookId, CustomUserDetails userDetails) {
        Book book = bookService.getBookById(bookId);

        return Objects.equals(book.getUser().getId(), userDetails.getId());
    }

    /**
     * Bypass the checking if the book selected is published
     * Only the author of the user can access the book if not published
     * @param bookId
     * @param userDetails
     * @return boolean
     */
    public boolean isPublished(UUID bookId, CustomUserDetails userDetails) {
        Book book = bookService.getBookById(bookId);
        if(book.getIsPublished()) return true;

        /**
         * Allow the collaborator to edit the book if the selected book is in draft
         */
        return userDetails != null && Objects.equals(book.getUser().getId(), userDetails.getId());
    }

    // TODO: Create a authorization for collaborator accessing any status of any book

}
