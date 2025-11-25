package Tproject.mapper;

import Tproject.dto.TaskDto;
import Tproject.dto.UserDto;
import Tproject.model.Task;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {
    abstract public TaskDto toDto(Task task);
    abstract public List<TaskDto> toListDto(List<Task> tasks);

    @AfterMapping
    protected void enrich(@MappingTarget TaskDto dto,Task task){
        dto.setCreator(new UserDto(task.getCreator().getUsername()));
        dto.setExecutor(new UserDto(task.getExecutor().getUsername()));
    }
}
