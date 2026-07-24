package com.uniread.user.dto.request;

import com.uniread.book.domain.entities.BookStatus;
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
