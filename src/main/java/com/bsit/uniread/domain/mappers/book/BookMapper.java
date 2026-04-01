package com.bsit.uniread.domain.mappers.book;

import com.bsit.uniread.application.dto.response.book.BookDto;
import com.bsit.uniread.domain.entities.book.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {


    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.profile.firstName")
    @Mapping(target = "displayName", source = "user.profile.displayName")
    @Mapping(target = "userPhoto", source = "user.profile.avatarUrl")
    BookDto toDto(Book book);

}
