package com.xcoder.tasklist.web.dto.task;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xcoder.tasklist.domain.task.Status;
import com.xcoder.tasklist.web.dto.validation.OnCreate;
import com.xcoder.tasklist.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Data
public class TaskDto {
    @NotNull(message = "Id must be not null.", groups = OnCreate.class)
    private Long id;
    @NotNull(message = "Title must be not null.", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Title length must be smaller than 255 symbol.",
        groups = {OnCreate.class, OnUpdate.class})
    private String title;
    @Length(max = 255, message = "Description length must be smaller than 255 symbol.",
        groups = {OnCreate.class, OnUpdate.class})
    private String description;
    private Status status;
    @DateTimeFormat(iso = ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime expirationDate;
}
