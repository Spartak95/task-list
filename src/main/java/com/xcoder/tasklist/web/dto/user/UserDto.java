package com.xcoder.tasklist.web.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.xcoder.tasklist.web.dto.validation.OnCreate;
import com.xcoder.tasklist.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserDto {
    @NotNull(message = "Name must be not null.", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Name length must be smaller than 255 symbols",
        groups = {OnCreate.class, OnUpdate.class})
    private String name;
    @NotNull(message = "Username must be not null.", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Username length must be smaller than 255 symbols",
        groups = {OnCreate.class, OnUpdate.class})
    private String username;
    @JsonProperty(access = Access.WRITE_ONLY)
    @NotNull(message = "Password must be not null.", groups = {OnCreate.class, OnUpdate.class})
    private String password;
    @JsonProperty(access = Access.WRITE_ONLY)
    @NotNull(message = "Password configuration must be not null.", groups = OnCreate.class)
    private String passwordConfirmation;
}
