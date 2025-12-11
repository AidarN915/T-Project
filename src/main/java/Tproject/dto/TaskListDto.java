package Tproject.dto;

import Tproject.model.Task;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskListDto extends AuditableDto {
    private Long id;
    private String title;
    private List<TaskDto> tasks;
}
