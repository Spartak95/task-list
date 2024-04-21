package com.xcoder.tasklist.service.impl;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import com.xcoder.tasklist.domain.MailType;
import com.xcoder.tasklist.domain.task.Task;
import com.xcoder.tasklist.domain.user.User;
import com.xcoder.tasklist.service.MailService;
import com.xcoder.tasklist.service.Reminder;
import com.xcoder.tasklist.service.TaskService;
import com.xcoder.tasklist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReminderImpl implements Reminder {
    private final TaskService taskService;
    private final UserService userService;
    private final MailService mailService;
    private static final Duration DURATION = Duration.ofHours(1);

    @Override
    //@Scheduled(cron = "0 * * * * *")
    public void remindForTask() {
        List<Task> tasks = taskService.getAllSoonTasks(DURATION);
        tasks.forEach(task -> {
            User user = userService.getTaskAuthor(task.getId());
            Properties properties = new Properties();
            properties.setProperty("task.title", task.getTitle());
            properties.setProperty("task.description", task.getDescription());
            mailService.sendEmail(user, MailType.REMINDER, properties);
        });
    }
}
