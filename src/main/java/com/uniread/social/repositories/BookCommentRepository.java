package com.uniread.social.repositories;

import com.uniread.book.domain.entities.Book;
import com.uniread.social.domain.entities.BookComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookCommentRepository
        extends JpaRepository<BookComment, UUID>, CrudRepository<BookComment, UUID>, JpaSpecificationExecutor<BookComment> {

    /**
     * Get the comments of a book
     * @param book
     * @param pageable
     * @return
     */
    Page<BookComment> findByBookAndParentBookCommentIsNotNull(Book book, Pageable pageable);

    /**
     * Get the replies of a comment
     * @param book
     * @param parentComment
     * @param pageable
     * @return
     */
    Page<BookComment> findByBookAndParentBookComment(Book book, BookComment parentComment, Pageable pageable);
}
