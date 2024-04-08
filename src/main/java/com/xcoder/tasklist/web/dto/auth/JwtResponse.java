package com.xcoder.tasklist.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Response for login")
public class JwtResponse {
    @Schema(description = "Login id", example = "1")
    private Long id;

    @Schema(description = "Username", example = "John Doe")
    private String username;

    @Schema(description = "Access token for login")
    private String accessToken;

    @Schema(description = "Refresh token for login")
    private String refreshToken;
}
