package Tproject.dto;


import Tproject.model.TaskImage;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime created;
    private LocalDateTime deadline;
    private boolean isDone;
    private UserDto executor;
    private UserDto creator;
    private Long chatRoomId;
    private List<TaskImageDto> taskImages;
}
