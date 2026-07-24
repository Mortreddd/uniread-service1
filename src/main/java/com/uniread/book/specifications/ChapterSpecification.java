package com.uniread.book.specifications;

import com.uniread.book.domain.entities.Chapter;
import com.uniread.book.domain.entities.ChapterStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ChapterSpecification {

    public static Specification<Chapter> hasBookById(UUID bookId) {
        return ((root, query, builder) -> {
            if(bookId == null) return null;

            return builder.equal(root.get("book").get("id"), bookId);
        });
    }


    public static Specification<Chapter> hasDeleted(String deletedAt) {
        return (root, query, builder) -> {
            if(deletedAt == null || deletedAt.isBlank()) return builder.isNull(root.get("deletedAt"));

            return builder.isNotNull(root.get("deletedAt"));
        };
    }

    public static Specification<Chapter> hasStatus(ChapterStatus status) {
        return (root, query, builder) -> status == null ? builder.conjunction() : builder.equal(root.get("status"), status);
    }

}
