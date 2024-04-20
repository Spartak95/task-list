package com.xcoder.tasklist.service.impl;

import static com.xcoder.tasklist.domain.user.Role.ROLE_USER;

import java.util.Optional;
import java.util.Set;

import com.xcoder.tasklist.config.TestConfig;
import com.xcoder.tasklist.domain.user.User;
import com.xcoder.tasklist.exception.ResourceNotFoundException;
import com.xcoder.tasklist.repository.TaskRepository;
import com.xcoder.tasklist.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ActiveProfiles("test")
@Import(TestConfig.class)
class UserServiceImplTest {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void getById() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
        User testUser = userService.getById(id);
        Mockito.verify(userRepository, Mockito.times(1)).findById(id);
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void getByIdWithNotExistingId() {
        Long id = 1L;
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.getById(id));
        Mockito.verify(userRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void getByUsername() {
        String username = "username";
        User user = new User();
        user.setUsername(username);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        User testUser = userService.getByUsername(username);
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void getByIdWithNotExistingUsername() {
        String username = "username";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.getByUsername(username));
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
    }

    @Test
    void update() {
        String password = "password";
        User user = new User();
        user.setPassword(password);
        userService.update(user);
        Mockito.verify(passwordEncoder).encode(password);
        Mockito.verify(userRepository).save(user);
    }

    @Test
    void isTaskOwner() {
        Long userId = 1L;
        Long taskId = 1L;
        Mockito.when(userRepository.isTaskOwner(userId, taskId)).thenReturn(Boolean.TRUE);
        boolean isOwner = userService.isTaskOwner(userId, taskId);
        Mockito.verify(userRepository).isTaskOwner(userId, taskId);
        Assertions.assertTrue(isOwner);
    }

    @Test
    void create() {
        String username = "username";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(password);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        User testUser = userService.create(user);
        Mockito.verify(userRepository).save(user);
        Mockito.verify(passwordEncoder).encode(password);
        Assertions.assertEquals(Set.of(ROLE_USER), testUser.getRoles());
    }

    @Test
    void createWithExistingUsername() {
        String username = "username";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(password);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));
        Assertions.assertThrows(IllegalStateException.class, () -> userService.create(user));
        Mockito.verify(userRepository, Mockito.never()).save(user);
    }

    @Test
    void createWithDifferentPassword() {
        String username = "username";
        String password = "password";
        String passwordConfirmation = "passwordConfirmation";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(passwordConfirmation);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalStateException.class, () -> userService.create(user));
        Mockito.verify(userRepository, Mockito.never()).save(user);
    }

    @Test
    void delete() {
        Long id = 1L;
        userService.delete(id);
        Mockito.verify(userRepository).deleteById(id);
    }
}