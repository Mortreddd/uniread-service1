package com.bsit.uniread.application.services.user;

import com.bsit.uniread.application.services.book.BookService;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookStatus;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.repositories.user.UserRepository;
import com.bsit.uniread.infrastructure.specifications.user.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final BookService bookService;
    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * Get the users or authors excluding the current authenticated user
     * The authors queried that matches the query
     * @param pageNo
     * @param pageSize
     * @param query
     * @param sortBy
     * @param orderBy
     * @param startDate
     * @param endDate
     * @param bannedAt
     * @param deletedAt
     * @return Pagination of Books
     */
    @Transactional(readOnly = true)
    @Cacheable(
            value = "authors",
            key = "T(java.util.Objects).hash(#pageNo, #pageSize, #query, #sortBy, #orderBy, #startDate, #endDate, #bannedAt, #deletedAt, #userDetails)"
    )
    public Page<User> getAuthors(
            int pageNo,
            int pageSize,
            String query,
            String sortBy,
            String orderBy,
            String startDate,
            String endDate,
            String bannedAt,
            String deletedAt,
            CustomUserDetails userDetails
    ) {
        Sort.Direction direction = sortBy.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, orderBy);

        Specification<User> authorSpecification = Specification
                .where(UserSpecification.hasBanned(bannedAt))
                .and(UserSpecification.hasDeleted(deletedAt))
                .and(UserSpecification.hasQuery(query))
                .and(UserSpecification.hasCurrentUser(userDetails));

        return userRepository.findAll(authorSpecification, PageRequest.of(pageNo, pageSize, sort));
    }
    /**
     * Get the user books based on given userId
     * @param userId
     * @param pageNo
     * @param pageSize
     * @param query
     * @param status
     * @param sortBy
     * @param orderBy
     * @return Pageable of books
     */
    public Page<Book> getAuthorBooksById(
            UUID userId,
            int pageNo,
            int pageSize,
            String query,
            BookStatus status,
            String sortBy,
            String orderBy
    ) {
        User user = userService.getUserById(userId);
        return bookService.getUserBooks(user, pageNo, pageSize, query, status, sortBy, orderBy);
    }

    public User getAuthorById(UUID authorId) {
        return userService.getUserById(authorId);
    }
}
