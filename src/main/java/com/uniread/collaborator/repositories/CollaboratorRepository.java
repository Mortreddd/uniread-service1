package com.uniread.collaborator.repositories;

import com.uniread.collaborator.domain.entities.Collaborator;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, UUID>, JpaSpecificationExecutor<Collaborator> {

    @EntityGraph(attributePaths = {"user", "book"})
    List<Collaborator> findByBookId(UUID bookId);
}
