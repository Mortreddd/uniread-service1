package com.bsit.uniread.application.services.user;

import com.bsit.uniread.application.dto.request.user.AuthorBookFilter;
import com.bsit.uniread.application.dto.request.user.AuthorFilter;
import com.bsit.uniread.application.dto.response.user.AuthorDetail;
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
     * @param filter
     * @param userDetails
     * @return Pagination of Books
     */
    @Transactional(readOnly = true)
    @Cacheable(
            value = "authors",
            key = "T(java.util.Objects).hash(" +
                    "#filter.pageNo, " +
                    "#filter.pageSize, " +
                    "#filter.query, " +
                    "#filter.sortBy, " +
                    "#filter.orderBy, " +
//                    "#filter.startDate, " +
//                    "#filter.endDate, " +
//                    "#filter.bannedAt, " +
//                    "#filter.deletedAt " +
                    ")"
    )
    public Page<AuthorDetail> getAuthors(
            AuthorFilter filter,
            CustomUserDetails userDetails
    ) {
        Sort.Direction direction = "asc".equalsIgnoreCase(filter.getSortBy()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, filter.getOrderBy());
        return userRepository.findAuthors(userDetails.getId(), filter.getBookStatus(), PageRequest.of(filter.getPageNo(), filter.getPageSize(), sort));
    }
    /**
     * Get the user books based on given userId
     * @param userId
     * @param filter
     * @return Pageable of books
     */
    public Page<Book> getAuthorBooksById(
            UUID userId,
            AuthorBookFilter filter
    ) {
        return bookService.getUserBooks(userId, filter);
    }

    /**
     * Since it was for public author, assuming the book status should be published
     * @param authorId
     * @param bookStatus
     * @return AuthorDetails
     */
    public AuthorDetail getAuthorById(UUID authorId, BookStatus bookStatus) {
        return userRepository.findByAuthorId(authorId, bookStatus)
                .orElse(null);
    }
}
