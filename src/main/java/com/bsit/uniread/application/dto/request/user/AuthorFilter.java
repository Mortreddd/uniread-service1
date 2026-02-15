package com.bsit.uniread.application.dto.request.user;


import com.bsit.uniread.domain.entities.book.BookStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class AuthorFilter {
    private Integer pageNo = 0;
    private Integer pageSize = 20;
    private String query;
    private String sortBy = "asc";
    private String orderBy = "createdAt";
    private BookStatus bookStatus = BookStatus.PUBLISHED;
    // TODO: Add startDate, endDate, bannedAt, deletedAt
}