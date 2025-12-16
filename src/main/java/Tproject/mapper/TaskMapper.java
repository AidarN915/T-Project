package Tproject.mapper;

import Tproject.dto.TaskDto;
import Tproject.dto.UserDto;
import Tproject.model.Task;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring",uses = {UserMapper.class,TaskUploadMapper.class, TaskMapper.class})
public abstract class TaskMapper {
    abstract public TaskDto toDto(Task task);
    abstract public List<TaskDto> toListDto(List<Task> tasks);
    @AfterMapping
    protected void enrich(@MappingTarget TaskDto dto,Task task) {
        if (task.getChatRoom() != null) {
            dto.setChatRoomId(task.getChatRoom().getId());
        }
        if(task.getParentTask() != null){
            dto.setParentTaskId(task.getParentTask().getId());
        }
    }

    @AfterMapping
    protected void postProcessList(List<Task> source,
                                   @MappingTarget List<TaskDto> target) {
        target.removeIf(dto -> dto.getParentTaskId() != null);
    }
}
