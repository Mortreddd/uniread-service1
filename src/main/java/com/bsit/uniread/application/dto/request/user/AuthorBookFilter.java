package com.bsit.uniread.application.dto.request.user;

import com.bsit.uniread.domain.entities.book.BookStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorBookFilter {
    private Integer pageNo = 0;
    private Integer pageSize = 10;
    private String query;
    private BookStatus status;
    private String sortBy = "asc";
    private String orderBy = "createdAt";
}
