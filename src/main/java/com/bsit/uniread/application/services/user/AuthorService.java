package com.bsit.uniread.application.services.user;

import com.bsit.uniread.application.dto.response.user.UserDto;
import com.bsit.uniread.application.services.book.BookService;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.repositories.book.BookRepository;
import com.bsit.uniread.infrastructure.repositories.user.UserRepository;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final BookService bookService;
    private final UserService userService;

    /**
     * Get the user books based on given userId
     * @param userId
     * @param pageNo
     * @param pageSize
     * @param query
     * @return Pageable of books
     */
    public Page<Book> getAuthorBooksById(UUID userId, int pageNo, int pageSize, String query) {
        User user = userService.getUserById(userId);
        return bookService.getUserBooks(user, pageNo, pageSize, query);
    }

    public User getAuthorById(UUID authorId) {
        return userService.getUserById(authorId);
    }
}
