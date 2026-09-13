package com.uniread.book.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.uniread.auth.domain.entities.User;
import com.uniread.common.domain.entities.ApprovalRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Data
@Table(name = "book_approval_requests")
@Entity
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BookApprovalRequest extends ApprovalRequest {

    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id")
    private Book book;

    private String reason;

    private Instant reviewedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    private String reviewerUsername;
    private String reviewerDisplayName;
}
