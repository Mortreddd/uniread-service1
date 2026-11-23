package com.uniread.admin.repositories;

import com.uniread.book.domain.entities.BookApprovalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminBookApprovalRepository extends JpaRepository<BookApprovalRequest, UUID> {
}
