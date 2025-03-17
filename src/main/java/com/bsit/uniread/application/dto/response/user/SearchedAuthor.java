package com.bsit.uniread.application.dto.response.user;

import com.bsit.uniread.application.dto.response.follow.FollowDto;
import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchedAuthor {
    private UserDto user;
    private Long storiesCount;
    private List<FollowDto> followings;
    private List<FollowDto> followers;

}
