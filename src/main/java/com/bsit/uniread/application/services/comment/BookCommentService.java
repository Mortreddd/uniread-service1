package com.bsit.uniread.application.services.comment;

import com.bsit.uniread.application.services.book.BookService;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookComment;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.repositories.book.BookCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookCommentService {

    private final BookService bookService;
    private final BookCommentRepository bookCommentRepository;

    /**
     * Get the book comments
     * @param bookId
     * @param pageNo
     * @param pageSize
     * @return page of book comments
     */
    @Transactional(readOnly = true)
    public Page<BookComment> getBookComments(UUID bookId, int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Book book = bookService.getBookById(bookId);
        return bookCommentRepository.findByBookAndParentBookCommentIsNotNull(book, pageable);
    }

    /**
     * Get the replies of a comment in book
     * @param bookId
     * @param parentCommentId
     * @param pageNo
     * @param pageSize
     * @return page of comment replies
     */
    @Transactional(readOnly = true)
    public Page<BookComment> getBookCommentReplies(UUID bookId, UUID parentCommentId, int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Book book = bookService.getBookById(bookId);
        BookComment parentComment = getBookCommentById(parentCommentId);

        return bookCommentRepository.findByBookAndParentBookComment(book, parentComment, pageable);
    }

    /**
     * Get the book comment using id
     * @param bookCommentId
     * @return bookComment
     */
    @Transactional(readOnly = true)
    public BookComment getBookCommentById(UUID bookCommentId) {
        return bookCommentRepository.findById(bookCommentId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to get comment"));
    }
}
