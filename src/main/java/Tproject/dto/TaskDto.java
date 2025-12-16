package Tproject.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TaskDto extends AuditableDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private boolean isDone;
    private List<UserDto> executors;
    private Long chatRoomId;
    private int priority;
    private List<TaskUploadDto> taskUploads;
    private List<TaskDto> subtasks;
    private Long parentTaskId;
}
