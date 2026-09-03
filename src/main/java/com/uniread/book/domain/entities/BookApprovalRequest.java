package com.uniread.book.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.uniread.auth.domain.entities.User;
import com.uniread.common.domain.entities.ApprovalRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Table(name = "book_approval_requests")
@Entity
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BookApprovalRequest extends ApprovalRequest {

    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    private String title;
    private String coverPhoto;
    private String description;
    private Integer readCount;
    private Boolean completed;
    private Boolean matured;

}
