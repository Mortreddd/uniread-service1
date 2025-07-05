package com.bsit.uniread.infrastructure.specifications.book;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookComment;
import org.springframework.data.jpa.domain.Specification;

public class BookCommentSpecification {

    public static Specification<BookComment> hasBook(Book book) {
        return (root, query, builder) -> builder.equal(root.get("book"), book);
    }

    public static Specification<BookComment> hasParentComment(BookComment parentComment) {
        return (root, query, builder) -> builder.equal(root.get("parentBookComment"), parentComment);
    }

}
