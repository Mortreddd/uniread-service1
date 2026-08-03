package com.uniread.book.domain.entities;

import com.uniread.common.domain.entities.ApprovalRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Table(name = "book_approval_requests")
@Entity
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BookApprovalRequest extends ApprovalRequest {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

}
