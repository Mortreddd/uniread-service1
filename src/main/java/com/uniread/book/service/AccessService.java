package com.uniread.book.service;

import java.util.UUID;

public interface AccessService {
    boolean canView(UUID resourceId, UUID userId);
}
