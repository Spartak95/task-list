package com.xcoder.tasklist.web.controller;

import java.util.List;

import com.xcoder.tasklist.domain.task.Task;
import com.xcoder.tasklist.domain.user.User;
import com.xcoder.tasklist.service.TaskService;
import com.xcoder.tasklist.service.UserService;
import com.xcoder.tasklist.web.dto.task.TaskDto;
import com.xcoder.tasklist.web.dto.user.UserDto;
import com.xcoder.tasklist.web.dto.validation.OnCreate;
import com.xcoder.tasklist.web.dto.validation.OnUpdate;
import com.xcoder.tasklist.web.mapper.TaskMapper;
import com.xcoder.tasklist.web.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "User API")
public class UserController {
    private final UserService userService;
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;

    @PutMapping
    @Operation(summary = "Get UserDto by id")
    @PreAuthorize("@customerSecurityExpression.canAccessUser(#userDto.id)")
    public UserDto update(@Validated(OnUpdate.class) @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User updatedUser = userService.update(user);
        return userMapper.toDto(updatedUser);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get UserDto by id")
    @PreAuthorize("@customerSecurityExpression.canAccessUser(#id)")
    public UserDto getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return userMapper.toDto(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id")
    @PreAuthorize("@customerSecurityExpression.canAccessUser(#id)")
    public void deleteById(@PathVariable Long id) {
        userService.delete(id);
    }

    @GetMapping("/{id}/tasks")
    @Operation(summary = "Get all user tasks")
    @PreAuthorize("@customerSecurityExpression.canAccessUser(#id)")
    public List<TaskDto> getTasksByUserId(@PathVariable Long id) {
        List<Task> tasks = taskService.getAllByUserId(id);
        return taskMapper.toDto(tasks);
    }

    @PostMapping("/{id}/tasks")
    @Operation(summary = "Add task to user")
    @PreAuthorize("@customerSecurityExpression.canAccessUser(#id)")
    public TaskDto createTask(@PathVariable Long id, @Validated(OnCreate.class) @RequestBody TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task createdTask = taskService.create(task, id);
        return taskMapper.toDto(createdTask);
    }
}
