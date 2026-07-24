package com.uniread.user.dto.response;

import com.uniread.social.dto.response.FollowDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchedAuthor {
    private UserDto user;
    private Long storiesCount;
    private List<FollowDto> followings;
    private List<FollowDto> followers;

}
