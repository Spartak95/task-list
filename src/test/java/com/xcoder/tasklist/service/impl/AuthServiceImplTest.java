package com.xcoder.tasklist.service.impl;

import java.util.Collections;
import java.util.Set;

import com.xcoder.tasklist.config.TestConfig;
import com.xcoder.tasklist.domain.user.Role;
import com.xcoder.tasklist.domain.user.User;
import com.xcoder.tasklist.exception.ResourceNotFoundException;
import com.xcoder.tasklist.repository.TaskRepository;
import com.xcoder.tasklist.repository.UserRepository;
import com.xcoder.tasklist.security.JwtTokenProvider;
import com.xcoder.tasklist.web.dto.auth.JwtRequest;
import com.xcoder.tasklist.web.dto.auth.JwtResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ActiveProfiles("test")
@Import(TestConfig.class)
class AuthServiceImplTest {
    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthServiceImpl authService;

    @Test
    void login() {
        Long userId = 1L;
        String username = "username";
        String password = "password";
        Set<Role> roles = Collections.emptySet();
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        JwtRequest request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(password);
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setRoles(roles);
        Mockito.when(userService.getByUsername(username)).thenReturn(user);
        Mockito.when(tokenProvider.createAccessToken(userId, username, roles)).thenReturn(accessToken);
        Mockito.when(tokenProvider.createRefreshToken(userId, username)).thenReturn(refreshToken);
        JwtResponse response = authService.login(request);
        Mockito.verify(authenticationManager)
            .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        Assertions.assertEquals(response.getUsername(), username);
        Assertions.assertEquals(response.getId(), userId);
        Assertions.assertNotNull(response.getAccessToken());
        Assertions.assertNotNull(response.getRefreshToken());
    }

    @Test
    void loginWithIncorrectUsername() {
        String username = "username";
        String password = "password";
        JwtRequest request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(password);
        Mockito.when(userService.getByUsername(username)).thenThrow(ResourceNotFoundException.class);
        Mockito.verifyNoInteractions(tokenProvider);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> authService.login(request));
    }

    @Test
    void refresh() {
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        String newRefreshToken = "newRefreshToken";
        JwtResponse response = new JwtResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(newRefreshToken);
        Mockito.when(tokenProvider.refreshUserTokens(refreshToken)).thenReturn(response);
        JwtResponse testResponse = authService.refresh(refreshToken);
        Mockito.verify(tokenProvider).refreshUserTokens(refreshToken);
        Assertions.assertEquals(response, testResponse);
    }
}