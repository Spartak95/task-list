package com.xcoder.tasklist.service;

import com.xcoder.tasklist.web.dto.auth.JwtRequest;
import com.xcoder.tasklist.web.dto.auth.JwtResponse;

public interface AuthService {
    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);
}
