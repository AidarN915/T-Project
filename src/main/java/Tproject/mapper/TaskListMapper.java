package Tproject.mapper;

import Tproject.dto.TaskListDto;
import Tproject.model.TaskList;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",uses = {TaskMapper.class})
public abstract class TaskListMapper {
    public abstract TaskListDto toDto(TaskList taskList);
    public abstract List<TaskListDto> toListDto(List<TaskList> taskListList);
}
