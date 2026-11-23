package com.uniread.admin.mappers;

import com.uniread.admin.dto.response.BookPendingApprovalDto;
import com.uniread.book.domain.entities.BookApprovalRequest;
import org.springframework.stereotype.Component;

@Component
public class AdminBookApprovalMapper {

    public BookPendingApprovalDto toPendingDto(
            BookApprovalRequest request
    ) {


        return BookPendingApprovalDto.builder()
                .build();
    }
}
