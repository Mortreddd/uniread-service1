package com.bsit.uniread.infrastructure.specifications.collaborator;

import com.bsit.uniread.domain.entities.collaborator.Collaborator;
import com.bsit.uniread.domain.entities.book.Book;
import org.springframework.data.jpa.domain.Specification;

public class CollaboratorSpecification {

    public static Specification<Collaborator> hasBook(Book book) {
        return (root, query, builder)
                -> book == null ? builder.conjunction() : builder.equal(root.get("book").get("id"), book.getId());
    }

}
