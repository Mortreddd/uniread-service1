package com.uniread.admin.dto.response;

import com.uniread.auth.domain.entities.User;
import com.uniread.common.domain.entities.ApprovalRequestStatus;
import com.uniread.common.domain.entities.ApprovalRequestType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class BookPendingApprovalDto {

    private UUID id;
    private BookDetail book;
    private UserDetail requester;


    private ApprovalRequestType requestType;
    private ApprovalRequestStatus requestStatus;


    private String reason;

    private Instant reviewedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private UserDetail reviewer;

    @Getter
    @Setter
    @Builder
    public static class UserDetail {
        private UUID id;
        private String username;
        private String fullName;
    }

    @Getter
    @Setter
    @Builder
    public static class BookDetail {
        private UUID id;
        private String title;
        private String description;
        private UserDetail fullName;
        private List<String> genres;
    }


}
