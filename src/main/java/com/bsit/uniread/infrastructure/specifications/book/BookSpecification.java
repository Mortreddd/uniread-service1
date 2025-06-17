package com.bsit.uniread.infrastructure.specifications.book;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookStatus;
import com.bsit.uniread.domain.entities.book.Genre;
import com.bsit.uniread.domain.entities.user.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class BookSpecification {

    public static Specification<Book> hasQuery(String query) {
        return (root, query1, cb) -> {
            if(query == null || query.isBlank()) return null;

            String likeQuery = "%" + query.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("title")), likeQuery),
                    cb.like(cb.lower(root.get("user").get("username")), likeQuery),
                    cb.like(cb.lower(root.get("user").get("lastName")), likeQuery),
                    cb.like(cb.lower(root.get("user").get("firstName")), likeQuery)
            );
        };
    }

    public static Specification<Book> hasStatus(BookStatus status) {
        return (root, query1, cb) -> {
            return status == null ? null :
                    cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Book> hasDeleted(String deletedAt) {
        return (root, query, cb) -> {
            if(deletedAt == null || deletedAt.isBlank()) {
                return cb.isNull(root.get("deletedAt"));
            }

            return cb.isNotNull(root.get("deletedAt"));
        };
    }

    public static Specification<Book> hasGenres(List<Integer> ids) {
        return (root, query, cb) -> {
            if(ids == null || ids.isEmpty()) return null;
            Join<Book, Genre> join = root.join("genres");
            query.distinct(true);
            return join.get("id").in(ids);
        };
    }

    public static Specification<Book> hasAuthor(User user) {
        return (root, query, builder) -> builder.equal(root.get("user"), user);
    }
}
