package com.xcoder.tasklist.domain.task;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Task {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime expirationDate;
}
