package com.xcoder.tasklist.web.dto.task;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.xcoder.tasklist.domain.task.Status;
import com.xcoder.tasklist.web.dto.validation.OnCreate;
import com.xcoder.tasklist.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Data
@Schema(description = "Request for task")
public class TaskDto {
    @Schema(description = "Task id", example = "1")
    @NotNull(message = "Id must be not null.", groups = OnUpdate.class)
    private Long id;

    @Schema(description = "Task title", example = "Go shopping")
    @NotNull(message = "Title must be not null.", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Title length must be smaller than 255 symbol.",
        groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @Schema(description = "Task description", example = "Go shopping and buy tables for office")
    @Length(max = 255, message = "Description length must be smaller than 255 symbol.",
        groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @Schema(description = "Task status", example = "ToDo")
    private Status status;

    @Schema(description = "Task expiration date", example = "2024-12-25 12:51")
    @DateTimeFormat(iso = ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime expirationDate;

    @JsonProperty(access = Access.READ_ONLY)
    private List<String> images;
}
