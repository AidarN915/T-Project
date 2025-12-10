package Tproject.dto;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
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
}
