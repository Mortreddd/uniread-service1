package com.uniread.social.specifications;

import com.uniread.book.domain.entities.Book;
import com.uniread.social.domain.entities.BookComment;
import org.springframework.data.jpa.domain.Specification;

public class BookCommentSpecification {

    public static Specification<BookComment> hasBook(Book book) {
        return (root, query, builder) -> builder.equal(root.get("book"), book);
    }

    public static Specification<BookComment> hasParentComment(BookComment parentComment) {
        return (root, query, builder) -> builder.equal(root.get("parentBookComment"), parentComment);
    }

}
