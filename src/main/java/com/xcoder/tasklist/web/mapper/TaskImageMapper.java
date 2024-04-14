package com.xcoder.tasklist.web.mapper;

import com.xcoder.tasklist.domain.task.TaskImage;
import com.xcoder.tasklist.web.dto.task.TaskImageDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskImageMapper extends Mappable<TaskImage, TaskImageDto> {

}
