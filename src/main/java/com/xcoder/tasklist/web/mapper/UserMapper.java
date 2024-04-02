package com.xcoder.tasklist.web.mapper;

import com.xcoder.tasklist.domain.user.User;
import com.xcoder.tasklist.web.dto.user.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDto dto);
}
