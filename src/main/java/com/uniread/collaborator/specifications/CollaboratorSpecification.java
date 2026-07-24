package com.uniread.collaborator.specifications;

import com.uniread.collaborator.domain.entities.Collaborator;
import com.uniread.book.domain.entities.Book;
import org.springframework.data.jpa.domain.Specification;

public class CollaboratorSpecification {

    public static Specification<Collaborator> hasBook(Book book) {
        return (root, query, builder)
                -> book == null ? builder.conjunction() : builder.equal(root.get("book").get("id"), book.getId());
    }

}
