package com.bsit.uniread.infrastructure.repositories.collaborator;

import com.bsit.uniread.domain.entities.collaborator.Collaborator;
import com.bsit.uniread.domain.entities.collaborator.CollaboratorRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CollaboratorRequestRepository extends JpaRepository<CollaboratorRequest, UUID>, CrudRepository<CollaboratorRequest, UUID>, JpaSpecificationExecutor<CollaboratorRequest> {

    @EntityGraph(attributePaths = {"user", "book"})
    Optional<CollaboratorRequest> findByBookIdAndId(UUID collaboratorRequestId, UUID bookId);
}
