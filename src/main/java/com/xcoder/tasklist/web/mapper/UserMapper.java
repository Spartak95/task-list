package com.xcoder.tasklist.web.mapper;

import com.xcoder.tasklist.domain.user.User;
import com.xcoder.tasklist.web.dto.user.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {
}
