package com.bsit.uniread.application.services.notification;

import com.bsit.uniread.application.dto.request.notification.NotificationFilter;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.notification.Notification;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.repositories.NotificationRepository;
import com.bsit.uniread.infrastructure.specifications.notification.NotificationSpecification;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

}
