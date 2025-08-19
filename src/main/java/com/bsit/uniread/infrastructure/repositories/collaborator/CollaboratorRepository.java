package com.bsit.uniread.infrastructure.repositories.collaborator;

import com.bsit.uniread.domain.entities.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, UUID>, CrudRepository<Collaborator, UUID>, JpaSpecificationExecutor<Collaborator> {

}
