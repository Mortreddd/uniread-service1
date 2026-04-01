package com.bsit.uniread.domain.mappers.user;

import com.bsit.uniread.application.dto.response.user.UserDto;
import com.bsit.uniread.domain.entities.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "firstName", source = "profile.firstName")
    @Mapping(target = "lastName", source = "profile.lastName")
    @Mapping(target = "displayName", source = "profile.displayName")
    @Mapping(target = "gender", source = "profile.gender")
    @Mapping(target = "avatarUrl", source = "profile.avatarUrl")
    @Mapping(target = "isEmailVerified", expression = "java(user.getEmailVerifiedAt() != null)")
    UserDto toDto(User user);
}
