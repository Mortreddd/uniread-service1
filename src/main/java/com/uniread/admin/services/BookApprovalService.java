package com.uniread.admin.services;

import com.uniread.admin.dto.response.BookPendingApprovalDto;
import com.uniread.admin.mappers.AdminBookApprovalMapper;
import com.uniread.admin.repositories.AdminBookApprovalRepository;
import com.uniread.auth.domain.entities.User;
import com.uniread.book.domain.entities.BookApprovalRequest;
import com.uniread.common.domain.entities.*;
import com.uniread.common.dto.request.ApprovalActionDto;
import com.uniread.common.dto.request.BookApprovalFilter;
import com.uniread.common.exceptions.ResourceNotFoundException;
import com.uniread.common.providers.CurrentUserProvider;
import com.uniread.common.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookApprovalService implements
        PendingListable<BookPendingApprovalDto, BookApprovalFilter>,
        Approvable<ApprovalActionDto>,
        Rejectable<ApprovalActionDto> {

    private final AdminBookApprovalRepository repository;
    private final CurrentUserProvider currentUser;
    private final AdminBookApprovalMapper mapper;

    @Override
    public Page<BookPendingApprovalDto> pendings(BookApprovalFilter filter) {


        var direction = filter != null && "asc".equalsIgnoreCase(filter.getSortBy())
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        var orderBy = filter != null ? filter.getOrderBy() : "createdAt"; // Order by column
        var sort = Sort.by(direction, orderBy);

        var pageable = PageRequest.of(
                filter != null ? filter.getPageNo() : 0,
                filter != null ? filter.getPageSize() : 20,
                sort
        );

        return repository.findAll(pageable)
                .map(mapper::toPendingDto);
    }

    @Override
    @Transactional
    public void approve(UUID requestId, ApprovalActionDto action) {
        var request = loadPending(requestId);
        var reviewer = currentUser.get();
        if(reviewer == null) {
            throw new IllegalStateException("Unable to approve without reviewer");
        }

        request.setRequestStatus(ApprovalRequestStatus.APPROVED);
        request.setReviewedAt(DateUtil.now());
        request.setReviewer(User.builder().id(reviewer.getId()).build());
        repository.save(request);
    }

    @Override
    @Transactional
    public void reject(UUID requestId, ApprovalActionDto action) {
        var request = loadPending(requestId);
        var reviewer = currentUser.get();
        if(reviewer == null) {
            throw new IllegalStateException("Unable to approve without reviewer");
        }

        request.setRequestStatus(ApprovalRequestStatus.REJECTED);
        request.setReviewedAt(DateUtil.now());
        request.setReviewer(User.builder().id(reviewer.getId()).build());
        repository.save(request);
    }

    private BookApprovalRequest loadPending(UUID id) {
        var req = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found: " + id));
        if (req.getRequestStatus() != ApprovalRequestStatus.PENDING) {
            throw new IllegalStateException("Request already reviewed");
        }
        return req;
    }


}
