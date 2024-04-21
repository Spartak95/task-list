package com.xcoder.tasklist.service;

import java.time.Duration;
import java.util.List;

import com.xcoder.tasklist.domain.task.Task;
import com.xcoder.tasklist.domain.task.TaskImage;

public interface TaskService {
    Task getById(Long id);

    List<Task> getAllByUserId(Long id);

    List<Task> getAllSoonTasks(Duration duration);

    Task update(Task task);

    Task create(Task task, Long userId);

    void delete(Long userId);

    void uploadImage(Long id, TaskImage image);
}
