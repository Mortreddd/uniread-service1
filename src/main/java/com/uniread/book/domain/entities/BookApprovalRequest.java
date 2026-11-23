package com.uniread.book.domain.entities;

import com.uniread.common.domain.entities.ApprovalRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Table(name = "book_approval_requests", indexes = {
        @Index(name = "idx_book_approval_requests_book_id", columnList = "book_id"),
        @Index(name = "idx_book_approval_requests_reviewer_id", columnList = "reviewer_id"),
        @Index(name = "idx_book_approval_requests_requester_id", columnList = "requester_id"),
})
@Entity
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BookApprovalRequest extends ApprovalRequest {


    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id")
    private Book book;

}
