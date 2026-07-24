package com.uniread.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ProfileDashboardDto {
    private Long totalCreatedBooks;
    private Long totalPublishedBooks;
    private Long totalDraftBooks;
    private Long totalRatings;
    private Long totalLikes;
    private Long totalReads;
    private Long totalFollowers;
    private Long totalFollowings;
}