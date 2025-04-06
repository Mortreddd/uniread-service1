package com.bsit.uniread.infrastructure.repositories.user;

import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository
        extends JpaRepository<User, UUID>, CrudRepository<User, UUID> {

    Optional<User> findByGoogleUuid(String googleUuid);
    // Search the users based on given email
    Optional<User> findByEmail(String email);
    // Search the users based on given username
    Optional<User> findByUsernameContainingIgnoreCase(String username);
    /**
     * Search the users based on a given parameter that match in any parameters
     * @param firstName
     * @param lastName
     * @param username
     * @param pageable
     * @source https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
     * @return pagination of users
     */
    Page<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrUsernameIgnoreCase(String firstName, String lastName, String username, Pageable pageable);

}
