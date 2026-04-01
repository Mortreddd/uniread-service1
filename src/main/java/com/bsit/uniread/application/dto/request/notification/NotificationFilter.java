package com.bsit.uniread.application.dto.request.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@NoArgsConstructor
@Setter
public class NotificationFilter {
    
    private Integer pageNo = 0;
    private Integer pageSize = 10;
    private String query;
    private String sortBy = "asc";
    private String orderBy = "createdAt";
    private Instant startDate;
    private Instant endDate;
    
}
