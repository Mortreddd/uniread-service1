package com.bsit.uniread.domain.entities.book;

import com.bsit.uniread.domain.entities.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
@Entity(name = "BookRating")
@Table(name = "book_ratings", indexes = {
        @Index(name = "idx_book_ratings_book_id", columnList = "book_id"),
        @Index(name = "idx_book_ratings_user_id", columnList = "user_id")
})
public class BookRating {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "book_id")
    private Book book;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private User user;

    private Integer rating;


}
