package com.uniread.admin.services;

import com.uniread.admin.dto.request.UserMonitoringFilter;
import com.uniread.admin.dto.response.UserMonitoringDto;
import com.uniread.admin.mapper.AdminUserMapper;
import com.uniread.admin.repositories.AdminUserRepository;
import com.uniread.admin.specifications.AdminUserSpecification;
import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.auth.domain.entities.User;
import com.uniread.common.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserMapper userMapper;
    private final AdminUserRepository userRepository;


    public Page<UserMonitoringDto> getUsersMonitor(
            UserMonitoringFilter filter,
            CustomUserDetails userDetails
    ) {

        var registeredAt = filter.getRegisteredAt() != null
                ? filter.getRegisteredAt().toInstant()
                : null;

        Specification<User> specification = Specification.allOf(
                AdminUserSpecification.hasQuery(filter.getQuery()),
                AdminUserSpecification.hasExcludedUser(userDetails.getId()),
                AdminUserSpecification.hasRegisteredAt(registeredAt)
        );

        Pageable pageable = PageRequest.of(
                filter.getPageNo(),
                filter.getPageSize()
        );

        return userRepository.findAll(specification, pageable)
                .map(userMapper::toUserMonitor);
    }

}
