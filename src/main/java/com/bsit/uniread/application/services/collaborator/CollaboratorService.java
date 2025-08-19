package com.bsit.uniread.application.services.collaborator;

import com.bsit.uniread.application.dto.request.collaborator.NewCollaboratorRequest;
import com.bsit.uniread.application.services.book.BookService;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.Collaborator;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.repositories.collaborator.CollaboratorRepository;
import com.bsit.uniread.infrastructure.specifications.collaborator.CollaboratorSpecification;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CollaboratorService {

    private final UserService userService;
    private final BookService bookService;
    private final CollaboratorRepository collaboratorRepository;

    /**
     * Get the collaborators of a book
     * @param bookId
     * @param pageNo
     * @param pageSize
     * @return pagination Collaborator
     */
    public Page<Collaborator> getBookCollaborators(
            UUID bookId,
            int pageNo,
            int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Book book = bookService.getBookById(bookId);
        Specification<Collaborator> collaboratorSpecification = Specification.where(
                CollaboratorSpecification.hasBook(book)
        );

        return collaboratorRepository.findAll(collaboratorSpecification, pageable);
    }

    /**
     * Get the collaborator by id
     * @param collaboratorId
     * @return collaborator
     */
    public Collaborator getCollaboratorById(UUID collaboratorId) {
        return collaboratorRepository.findById(collaboratorId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find collaborator"));
    }

    /**
     * Approved and create a collaborator
     * @param bookId
     * @param request
     * @return collaborator
     */
    public Collaborator createCollaborator(UUID bookId, NewCollaboratorRequest request) {
        Book book = bookService.getBookById(bookId);
        User user = userService.getUserById(request.getUserCollaboratorId());

        Collaborator collaborator = Collaborator.builder()
                .book(book)
                .user(user)
                .permissions(List.of(request.getPermissions()))
                .bannedAt(null)
                .build();

        return save(collaborator);
    }

    /**
     * Store and retrieve a collaborator in database
     * @param collaborator
     * @return collaborator
     */
    public Collaborator save(Collaborator collaborator) {
        return collaboratorRepository.save(collaborator);
    }


    /**
     * Getting the collaborator and ban a collaborator by setting bannedAt into now() method
     * @param collaboratorId
     * @return collaborator
     */
    public Collaborator banCollaborator(UUID collaboratorId) {
        Collaborator collaborator = getCollaboratorById(collaboratorId);
        collaborator.setBannedAt(DateUtil.now());

        // TODO: Maybe adding a notification for being banned and add a event listener

        return save(collaborator);
    }

    public Collaborator unbanCollaborator(UUID collaboratorId) {
        Collaborator collaborator = getCollaboratorById(collaboratorId);
        collaborator.setBannedAt(null);

        return save(collaborator);
    }

    public void deleteCollaborator(UUID collaboratorId) {

        // TODO: Maybe include a notification for being kicked out by adding event listener
        collaboratorRepository.deleteById(collaboratorId);

    }
}
