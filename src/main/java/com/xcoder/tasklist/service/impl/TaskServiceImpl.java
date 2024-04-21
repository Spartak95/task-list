package com.xcoder.tasklist.service.impl;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import com.xcoder.tasklist.domain.task.Status;
import com.xcoder.tasklist.domain.task.Task;
import com.xcoder.tasklist.domain.task.TaskImage;
import com.xcoder.tasklist.exception.ResourceNotFoundException;
import com.xcoder.tasklist.repository.TaskRepository;
import com.xcoder.tasklist.service.ImageService;
import com.xcoder.tasklist.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
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
    @Transactional(readOnly = true)
    public List<Task> getAllSoonTasks(Duration duration) {
        LocalDateTime now = LocalDateTime.now();
        return taskRepository.findAllSoonTasks(Timestamp.valueOf(now), Timestamp.valueOf(now.plus(duration)));
    }

    @Override
    @Transactional
    @CachePut(value = "TaskService::getById", key = "#task.id")
    public Task update(Task task) {
        Task existing = getById(task.getId());
        if (task.getStatus() == null) {
            existing.setStatus(Status.TODO);
        } else {
            existing.setStatus(task.getStatus());
        }
        existing.setTitle(task.getTitle());
        existing.setDescription(task.getDescription());
        existing.setExpirationDate(task.getExpirationDate());
        taskRepository.save(task);

        return task;
    }

    @Override
    @Transactional
    @Cacheable(value = "TaskService::getById", condition = "#task.id!=null", key = "#task.id")
    public Task create(Task task, Long userId) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }

        taskRepository.save(task);
        taskRepository.assignTask(userId, task.getId());

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
        String fileName = imageService.upload(image);
        taskRepository.addImage(id, fileName);
    }
}
