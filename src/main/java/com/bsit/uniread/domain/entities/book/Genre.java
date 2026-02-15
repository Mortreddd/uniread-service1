package com.bsit.uniread.domain.entities.book;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "genres")
@Entity
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;
    private String backgroundImage;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "book_genres",
            joinColumns = { @JoinColumn(name = "genre_id")},
            inverseJoinColumns = { @JoinColumn(name = "book_id")})
    @JsonBackReference
    public List<Book> books;

    @JsonSerialize(using = ToStringSerializer.class)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonSerialize(using = ToStringSerializer.class)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
