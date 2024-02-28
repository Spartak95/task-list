package com.xcoder.tasklist.web.mapper;

import java.util.List;

import com.xcoder.tasklist.domain.task.Task;
import com.xcoder.tasklist.web.dto.task.TaskDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto toDto(Task task);
    List<TaskDto> toDto(List<Task> tasks);
    Task toEntity(TaskDto dto);
}
