package com.uniread.collaborator.specifications;

import com.uniread.collaborator.domain.entities.CollaboratorRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class CollaboratorRequestSpecification {

    public static Specification<CollaboratorRequest> hasBook(UUID bookId) {
        return (root, query, builder) -> bookId == null ?
                builder.conjunction() :
                builder.equal(root.get("book").get("id"), bookId);
    }

}
