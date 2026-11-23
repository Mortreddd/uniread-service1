package com.uniread.common.domain.entities;

import com.uniread.common.dto.request.ApprovalActionDto;

import java.util.UUID;

public interface Rejectable<T> {
    void reject(UUID requestId, T action);
}
