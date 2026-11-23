package com.uniread.user.service;

import com.uniread.common.exceptions.ResourceNotFoundException;
import com.uniread.common.providers.CurrentUserProvider;
import com.uniread.social.domain.entities.Follow;
import com.uniread.user.dto.request.PublicUserFilter;
import com.uniread.user.dto.response.AuthorProfileDetail;
import com.uniread.user.mappers.PublicUserMapper;
import com.uniread.user.repositories.PublicUserRepository;
import com.uniread.user.repositories.UserFollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PublicUserService {

    private final UUID ANONYMOUS = new UUID(0, 0);
    private final CurrentUserProvider currentUserProvider;
    private final PublicUserMapper mapper;
    private final PublicUserRepository publicUserRepository;
    private final UserFollowRepository userFollowRepository;


    public Page<AuthorProfileDetail> getPublicUsers(
            PublicUserFilter filter
    ) {
        var direction = filter != null && "asc".equalsIgnoreCase(filter.getSortBy())
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        var orderBy = filter != null ? filter.getOrderBy() : "u1.createdAt"; // Order by column
        var sort = Sort.by(direction, orderBy);

        var pageable = PageRequest.of(
                filter != null ? filter.getPageNo() : 0,
                filter != null ? filter.getPageSize() : 20,
                sort
        );

        var currentUser = currentUserProvider.get();
        boolean anonymous = currentUser == null;
        UUID currentUserId = anonymous ? ANONYMOUS : currentUser.getId();

        Page<AuthorProfileDetail> page =
                publicUserRepository.findPublicUsers(currentUserId, pageable);

        if (anonymous) {
            return page.map(dto -> mapper.toProfileDetail(dto, null, List.of()));
        }

        List<UUID> authorIds = page.getContent().stream()
                .map(AuthorProfileDetail::getId)
                .toList();

        List<Follow> edges = userFollowRepository.findEdgesBetween(currentUserId, authorIds);

        return page.map(dto -> mapper.toProfileDetail(dto, currentUserId, edges));
    }

//
//    public AuthorProfileDetail getPublicUser(String username) {
//        return publicUserRepository.findPublicUserByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("Unable to find user with username " + username));
//    }


}
