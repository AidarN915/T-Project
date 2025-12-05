package Tproject.mapper;

import Tproject.dto.TaskImageDto;
import Tproject.model.TaskImage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TaskImageMapper {
    public abstract TaskImageDto toDto(TaskImage taskImage);
    public abstract List<TaskImageDto> toListDto(List<TaskImage> taskImage);
}
