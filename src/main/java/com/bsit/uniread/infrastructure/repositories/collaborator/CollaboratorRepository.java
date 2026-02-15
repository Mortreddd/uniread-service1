package com.bsit.uniread.infrastructure.repositories.collaborator;

import com.bsit.uniread.domain.entities.collaborator.Collaborator;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, UUID>, CrudRepository<Collaborator, UUID>, JpaSpecificationExecutor<Collaborator> {

    @EntityGraph(attributePaths = {"user", "book"})
    List<Collaborator> findByBookId(UUID bookId);
}
