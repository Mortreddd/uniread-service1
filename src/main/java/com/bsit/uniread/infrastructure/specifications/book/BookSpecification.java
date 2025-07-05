package com.bsit.uniread.infrastructure.specifications.book;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookStatus;
import com.bsit.uniread.domain.entities.book.Genre;
import com.bsit.uniread.domain.entities.user.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public class BookSpecification {

    /**
     * Create a condition for a search or query
     * It query the username, firstName, lastName of the owner and title of the book
     * @param query
     * @return org.springframework.data.jpa.domain.Specification
     */
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

    /**
     * Creates a condition for a book depending on the status given
     * if status is null, it bypasses the condition instead
     * @param status
     * @return org.springframework.data.jpa.domain.Specification
     */
    public static Specification<Book> hasStatus(BookStatus status) {
        return (root, query1, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    /**
     * Create a condition for book for soft delete
     * Giving null as a parameter will bypass the condition
     * @param deletedAt
     * @return org.springframework.data.jpa.domain.Specification
     */
    public static Specification<Book> hasDeleted(String deletedAt) {
        return (root, query, cb) -> {
            if(deletedAt == null || deletedAt.isBlank()) {
                return cb.isNull(root.get("deletedAt"));
            }

            return cb.isNotNull(root.get("deletedAt"));
        };
    }

    /**
     * Create a condition for book matching given genre ids
     * Providing empty list of genre ids will bypass the condition
     * @param ids
     * @return org.springframework.data.jpa.domain.Specification
     */
    public static Specification<Book> hasGenres(List<Integer> ids) {
        return (root, query, cb) -> {
            if(ids == null || ids.isEmpty()) return null;
            Join<Book, Genre> join = root.join("genres");
            query.distinct(true);
            return join.get("id").in(ids);
        };
    }

    /**
     * Create a condition for book matching the provided user
     * @param user
     * @return org.springframework.data.jpa.domain.Specification
     */
    public static Specification<Book> hasAuthor(User user) {
        return (root, query, builder) -> builder.equal(root.get("user"), user);
    }

    /**
     * Create a condition for book matching the provider user id
     * @param userId
     * @return org.springframework.data.jpa.domain.Specification
     */
    public static Specification<Book> hasAuthorById(UUID userId) {
        return (root, query, builder) -> builder.equal(root.get("user").get("id"), userId);
    }
}
