package com.uniread.common.domain.entities;

import com.uniread.common.dto.request.ApprovalActionDto;

import java.util.UUID;

public interface Approvable <T> {

    void approve(UUID requestId, T action);
}
