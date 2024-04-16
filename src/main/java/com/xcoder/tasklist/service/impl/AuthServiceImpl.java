package com.xcoder.tasklist.service.impl;

import com.xcoder.tasklist.domain.user.User;
import com.xcoder.tasklist.security.JwtTokenProvider;
import com.xcoder.tasklist.service.AuthService;
import com.xcoder.tasklist.service.UserService;
import com.xcoder.tasklist.web.dto.auth.JwtRequest;
import com.xcoder.tasklist.web.dto.auth.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        authenticationManager.authenticate(authentication);
        User user = userService.getByUsername(loginRequest.getUsername());
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setId(user.getId());
        jwtResponse.setUsername(loginRequest.getUsername());
        jwtResponse.setAccessToken(
            jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(), user.getRoles()));
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getUsername()));

        return jwtResponse;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }
}
