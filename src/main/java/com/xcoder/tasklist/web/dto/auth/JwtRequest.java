package com.xcoder.tasklist.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request for login")
public class JwtRequest {
    @Schema(description = "User email", example = "johndoe@gmail.com")
    @NotNull(message = "Username must be not null")
    private String username;
    @Schema(description = "User password")
    @NotNull(message = "Password must be not null")
    private String password;
}
