package com.bsit.uniread.application.services.collaborator;

import com.bsit.uniread.application.controllers.collaborator.RequestStatus;
import com.bsit.uniread.application.dto.request.collaborator.EditBookCollaboratorRequest;
import com.bsit.uniread.application.dto.request.collaborator.NewCollaboratorRequest;
import com.bsit.uniread.application.services.book.BookService;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.collaborator.CollaboratorRequest;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.domain.events.collaborator.NewBookCollaboratorEvent;
import com.bsit.uniread.infrastructure.handler.dispatcher.collaborator.CollaboratorEventPublisher;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.repositories.collaborator.CollaboratorRequestRepository;
import com.bsit.uniread.infrastructure.specifications.collaborator.CollaboratorRequestSpecification;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CollaboratorRequestService {

    private final CollaboratorService collaboratorService;
    private final UserService userService;
    private final BookService bookService;
    private final CollaboratorRequestRepository collaboratorRequestRepository;
    private final CollaboratorEventPublisher collaboratorEventPublisher;

    /**
     * Get the collaborators of a book
     * @param bookId
     * @param pageNo
     * @param pageSize
     * @return collaboratorRequest
     */
    @Transactional(readOnly = true)
    public Page<CollaboratorRequest> getBookCollaborationRequests(
            UUID bookId,
            int  pageNo,
            int pageSize
    ) {

        String sortBy = "createdAt";
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Specification<CollaboratorRequest> specification = Specification.where(
                CollaboratorRequestSpecification.hasBook(bookId)
        );

        return collaboratorRequestRepository.findAll(specification, pageable);
    }

    @Transactional
    public CollaboratorRequest getBookCollaboratorRequestById(UUID bookId, UUID collaboratorRequestId) {
        return collaboratorRequestRepository.findByBookIdAndId(bookId, collaboratorRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find book collaboration request"));
    }

    /**
     * Create a new request for collaborating with the selected book
     * @param bookId
     * @param userDetails
     * @return collaborationRequest
     */
    @Transactional
    public CollaboratorRequest createBookCollaboratorRequest(
            UUID bookId,
            NewCollaboratorRequest collaboratorRequest,
            CustomUserDetails userDetails
    ) {
        Book book = bookService.getBookById(bookId);
        User user = userService.getUserById(userDetails.getId());
        CollaboratorRequest request = CollaboratorRequest.builder()
                .book(book)
                .user(user)
                .status(RequestStatus.PENDING)
                .message(collaboratorRequest.getMessage())
                .createdAt(DateUtil.now())
                .build();

        return save(request);
    }

    @Transactional
    public CollaboratorRequest editCollaboratorRequest(
            UUID bookId,
            UUID collaboratorRequestId,
            EditBookCollaboratorRequest request,
            CustomUserDetails userDetails
    ) {
        CollaboratorRequest collaboratorRequest = getBookCollaboratorRequestById(bookId, collaboratorRequestId);
        if(request.getStatus() == RequestStatus.REJECTED) {
            collaboratorRequest.setStatus(RequestStatus.REJECTED);
            return save(collaboratorRequest);
        }

        collaboratorRequest.setStatus(request.getStatus());
        collaboratorEventPublisher.approvedCollaboratorRequest(collaboratorRequest.getBook(), collaboratorRequest.getUser());
        return save(collaboratorRequest);

    }

    /**
     * Save the collaboration request
     * @param request
     * @return collaboratorRequest
     */
    @Transactional
    public CollaboratorRequest save(CollaboratorRequest request) {
        return collaboratorRequestRepository.save(request);
    }
}
