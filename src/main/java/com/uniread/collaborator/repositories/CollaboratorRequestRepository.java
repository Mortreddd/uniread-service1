package com.uniread.collaborator.repositories;

import com.uniread.collaborator.domain.entities.CollaboratorRequest;
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
