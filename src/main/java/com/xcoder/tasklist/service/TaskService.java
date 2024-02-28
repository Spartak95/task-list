package com.xcoder.tasklist.service;

import java.util.List;

import com.xcoder.tasklist.domain.task.Task;

public interface TaskService {
    Task getById(Long id);
    List<Task> getAllByUserId(Long id);
    Task update(Task task);
    Task create(Task task, Long id);
    void delete(Long id);
}
