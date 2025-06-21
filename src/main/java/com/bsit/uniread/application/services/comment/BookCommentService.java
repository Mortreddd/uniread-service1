package com.bsit.uniread.application.services.comment;

import com.bsit.uniread.application.services.book.BookService;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookComment;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.repositories.book.BookCommentRepository;
import com.bsit.uniread.infrastructure.specifications.book.BookCommentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookCommentService {

    private final BookService bookService;
    private final UserService userService;
    private final BookCommentRepository bookCommentRepository;

    /**
     * Get the comment by id
     * @param parentCommentId
     * @return bookComment
     */
    @Transactional(readOnly = true)
    public BookComment getBookCommentById(UUID parentCommentId) {
        return bookCommentRepository.findById(parentCommentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
    }

    /**
     * Get the comments of a book using pagination
     * @param bookId
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param orderBy
     * @param startDate
     * @param endDate
     * @return pagination of comments
     */
    @Transactional(readOnly = true)
    public Page<BookComment> getBookComments(
            UUID bookId,
            int pageNo,
            int pageSize,
            String sortBy,
            String orderBy,
            String startDate,
            String endDate
    ) {
        Book book = bookService.getBookById(bookId);
        Sort.Direction direction = sortBy.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, orderBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Specification<BookComment> bookCommentSpecification = Specification
                .where(BookCommentSpecification.hasBook(book));

        return bookCommentRepository.findAll(bookCommentSpecification, pageable);
    }

    /**
     * Get the replies of a parent comment in a book using pagination
     * @param bookId
     * @param bookParentCommentId
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param orderBy
     * @param startDate
     * @param endDate
     * @return pagination of replies
     */
    @Transactional(readOnly = true)
    public Page<BookComment> getBookCommentReplies(
            UUID bookId,
            UUID bookParentCommentId,
            int pageNo,
            int pageSize,
            String sortBy,
            String orderBy,
            String startDate,
            String endDate
    ) {
        Book book = bookService.getBookById(bookId);
        BookComment parentComment = getBookCommentById(bookParentCommentId);
        Sort.Direction direction = sortBy.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, orderBy);

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Specification<BookComment> bookCommentSpecification = Specification
                .where(BookCommentSpecification.hasBook(book))
                .and(BookCommentSpecification.hasParentComment(parentComment));

        return bookCommentRepository.findAll(bookCommentSpecification, pageable);
    }

    /**
     * Create a book feedback which automatically a parent comment for the book
     * @param bookId
     * @param userDetails
     * @param content
     * @param rating
     * @return bookComment
     */
    @Transactional
    public BookComment createBookComment(
            UUID bookId,
            CustomUserDetails userDetails,
            String content,
            int rating
    ) {
        Book book = bookService.getBookById(bookId);
        User user = userService.getUserById(userDetails.getId());

        BookComment comment = BookComment
                .builder()
                .book(book)
                .user(user)
                .content(content)
                .rating(rating)
                .build();

        return bookCommentRepository.save(comment);
    }

    /**
     * Create a reply to a parent comment from a book feedback
     * @param bookId
     * @param parentBookCommentId
     * @param commenterId
     * @param content
     * @param rating
     * @return childBookComment
     */
    @Transactional
    public BookComment createBookCommentReply(
            UUID bookId,
            UUID parentBookCommentId,
            UUID commenterId,
            String content,
            int rating
    ) {

        Book book = bookService.getBookById(bookId);
        User user = userService.getUserById(commenterId);
        BookComment parentBookComment = getBookCommentById(parentBookCommentId);

        BookComment comment = BookComment
                .builder()
                .book(book)
                .user(user)
                .parentBookComment(parentBookComment)
                .content(content)
                .rating(rating)
                .build();

        return bookCommentRepository.save(comment);
    }

}
