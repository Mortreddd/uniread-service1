package com.bsit.uniread.domain.entities.book;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Table(name = "tags", indexes = {
        @Index(name = "idx_tags_name", columnList = "name"),
})
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "book_tags", joinColumns = {
            @JoinColumn(name = "tag_id")
    }, inverseJoinColumns = {
            @JoinColumn(name = "book_id")
    }, indexes = {
            @Index(name = "idx_book_tags_book_id", columnList = "book_id"),
            @Index(name = "idx_book_tags_tag_id", columnList = "tag_id")
    })
    private List<Book> books = new ArrayList<>();

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
