package com.xcoder.tasklist.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.xcoder.tasklist.config.TestConfig;
import com.xcoder.tasklist.domain.task.Status;
import com.xcoder.tasklist.domain.task.Task;
import com.xcoder.tasklist.domain.task.TaskImage;
import com.xcoder.tasklist.exception.ResourceNotFoundException;
import com.xcoder.tasklist.repository.TaskRepository;
import com.xcoder.tasklist.repository.UserRepository;
import com.xcoder.tasklist.service.ImageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ActiveProfiles("test")
@Import(TestConfig.class)
class TaskServiceImplTest {
    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private ImageService imageService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private TaskServiceImpl taskService;

    @Test
    void getById() {
        Long id = 1L;
        Task task = new Task();
        task.setId(id);
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        Task testTask = taskService.getById(id);
        Mockito.verify(taskRepository, Mockito.times(1)).findById(id);
        Assertions.assertEquals(task, testTask);
    }

    @Test
    void getByIdWithNotExistingId() {
        Long id = 1L;
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> taskService.getById(id));
        Mockito.verify(taskRepository).findById(id);
    }

    @Test
    void getAllByUserId() {
        Long userId = 1L;
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            tasks.add(new Task());
        }
        Mockito.when(taskRepository.findAllByUserId(userId)).thenReturn(tasks);
        List<Task> testTasks = taskService.getAllByUserId(userId);
        Mockito.verify(taskRepository).findAllByUserId(userId);
        Assertions.assertEquals(tasks, testTasks);
    }

    @Test
    void update() {
        Long id = 1L;
        Task task = new Task();
        task.setId(id);
        task.setStatus(Status.DONE);
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        Task testTask = taskService.update(task);
        Mockito.verify(taskRepository).save(task);
        Assertions.assertEquals(task, testTask);
        Assertions.assertEquals(task.getTitle(), testTask.getTitle());
        Assertions.assertEquals(task.getDescription(), testTask.getDescription());
        Assertions.assertEquals(task.getStatus(), testTask.getStatus());
    }

    @Test
    void updateWithNullStatus() {
        Long id = 1L;
        Task task = new Task();
        task.setId(id);
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        Task testTask = taskService.update(task);
        Mockito.verify(taskRepository).save(task);
        Assertions.assertEquals(task.getTitle(), testTask.getTitle());
        Assertions.assertEquals(task.getDescription(), testTask.getDescription());
        Assertions.assertEquals(Status.TODO, testTask.getStatus());
    }

    @Test
    void create() {
        Long taskId = 1L;
        Long userId = 1L;
        Task task = new Task();
        Mockito.doAnswer(invocationOnMock -> {
                Task savedTask = invocationOnMock.getArgument(0);
                savedTask.setId(taskId);
                return savedTask;
            })
            .when(taskRepository).save(task);
        Task testTask = taskService.create(task, userId);
        Mockito.verify(taskRepository).save(task);
        Assertions.assertNotNull(testTask.getId());
        Mockito.verify(taskRepository).assignTask(userId, task.getId());
    }

    @Test
    void delete() {
        Long id = 1L;
        taskService.delete(id);
        Mockito.verify(taskRepository).deleteById(id);
    }

    @Test
    void uploadImage() {
        Long id = 1L;
        String imageName = "imageName";
        TaskImage taskImage = new TaskImage();
        Mockito.when(imageService.upload(taskImage)).thenReturn(imageName);
        taskService.uploadImage(id, taskImage);
        Mockito.verify(taskRepository).addImage(id, imageName);
    }
}
