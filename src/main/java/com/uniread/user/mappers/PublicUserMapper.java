package com.uniread.user.mappers;

import com.uniread.common.services.CloudinaryService;
import com.uniread.social.domain.entities.Follow;
import com.uniread.user.dto.response.AuthorProfileDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PublicUserMapper {

    private final CloudinaryService cloudinaryService;

    public AuthorProfileDetail toProfileDetail(
            AuthorProfileDetail dto, UUID currentUserId, List<Follow> edges) {
        UUID authorId = dto.getId();
        boolean isFollowing = edges.stream().anyMatch(f ->
                f.getFollower().getId().equals(currentUserId)
                        && f.getFollowing().getId().equals(authorId));
        boolean isFollower = edges.stream().anyMatch(f ->
                f.getFollowing().getId().equals(currentUserId)
                        && f.getFollower().getId().equals(authorId));

        dto.setIsFollowing(isFollowing);
        dto.setIsFollower(isFollower);
        dto.setIsMutualFollowing(isFollowing && isFollower);
        dto.setAvatarUrl(cloudinaryService.generatePublicUrl(dto.getAvatarUrl()));
        dto.setCoverUrl(cloudinaryService.generatePublicUrl(dto.getCoverUrl()));
        return dto;
    }

}
