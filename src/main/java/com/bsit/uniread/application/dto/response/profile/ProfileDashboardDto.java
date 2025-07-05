package com.bsit.uniread.application.dto.response.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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