package com.xcoder.tasklist.service.impl;

import java.util.List;

import com.xcoder.tasklist.domain.task.Status;
import com.xcoder.tasklist.domain.task.Task;
import com.xcoder.tasklist.domain.task.TaskImage;
import com.xcoder.tasklist.domain.user.User;
import com.xcoder.tasklist.exception.ResourceNotFoundException;
import com.xcoder.tasklist.repository.TaskRepository;
import com.xcoder.tasklist.service.ImageService;
import com.xcoder.tasklist.service.TaskService;
import com.xcoder.tasklist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final UserService userService;
    private final ImageService imageService;
    private final TaskRepository taskRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "TaskService::getById", key = "#id")
    public Task getById(Long id) {
        return taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllByUserId(Long id) {
        return taskRepository.findAllByUserId(id);
    }

    @Override
    @Transactional
    @CachePut(value = "TaskService::getById", key = "#task.id")
    public Task update(Task task) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }

        taskRepository.save(task);

        return task;
    }

    @Override
    @Transactional
    @Cacheable(value = "TaskService::getById", key = "#task.id")
    public Task create(Task task, Long userId) {
        User user = userService.getById(userId);
        task.setStatus(Status.TODO);
        taskRepository.save(task);
        taskRepository.assignTask(task.getId(), userId);
        user.getTasks().add(task);
        userService.update(user);

        return task;
    }

    @Override
    @Transactional
    @CacheEvict(value = "TaskService::getById", key = "#id")
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "TaskService::getById", key = "#id")
    public void uploadImage(Long id, TaskImage image) {
        Task task = getById(id);
        String filename = imageService.upload(image);
        task.getImages().add(filename);
        taskRepository.save(task);
    }
}
