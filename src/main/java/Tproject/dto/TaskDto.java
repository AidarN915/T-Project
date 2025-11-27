package Tproject.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime created;
    private LocalDateTime deadline;
    private boolean isDone;
    private TaskListDto taskList;
    private UserDto executor;
    private UserDto creator;
}
