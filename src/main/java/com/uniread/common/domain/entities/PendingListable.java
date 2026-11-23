package com.uniread.common.domain.entities;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PendingListable<T, Filter> {
    Page<T> pendings(Filter filter);
}
