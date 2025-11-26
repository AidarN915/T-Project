package Tproject.dto;

import Tproject.model.Task;
import lombok.Data;

import java.util.List;

@Data
public class TaskListDto {
    private Long id;
    private String title;
    private List<TaskDto> tasks;
    private UserDto user;
}
