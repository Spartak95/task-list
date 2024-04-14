package com.xcoder.tasklist.web.mapper;

import com.xcoder.tasklist.domain.task.Task;
import com.xcoder.tasklist.web.dto.task.TaskDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper extends Mappable<Task, TaskDto> {

}
