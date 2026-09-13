package com.uniread.user.dto.request;


import com.uniread.book.domain.entities.BookStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserBookFilter {
    private int pageNo = 0;
    private int pageSize = 10;
    private String query = "";

    private List<Integer> genres;
    private BookStatus status;
    private String sortBy = "createdAt";
    private String orderBy = "asc";

    private String startDate;
    private String endDate;
    private String deletedAt;

}