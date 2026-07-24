package com.uniread.social.repositories;

import com.uniread.book.domain.entities.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, UUID>, CrudRepository<Bookmark, UUID> {
}
