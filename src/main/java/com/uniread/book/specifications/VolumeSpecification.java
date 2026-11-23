package com.uniread.book.specifications;

import com.uniread.book.domain.entities.Volume;
import com.uniread.book.domain.entities.VolumeStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class VolumeSpecification {

    public static Specification<Volume> hasBook(UUID bookId) {
        if(bookId == null) return null;

        return (root, query, builder) -> {
            return builder.equal(root.get("book").get("id"), bookId);
        };
    }

    public static Specification<Volume> hasStatus(VolumeStatus status) {
        if(status == null) return null;

        return ((root, query, builder) -> {
            return builder.equal(root.get("status"), status);
        });
    }


}
