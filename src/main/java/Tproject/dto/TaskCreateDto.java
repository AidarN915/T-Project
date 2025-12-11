package Tproject.dto;

import Tproject.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TaskCreateDto {
    private String title;
    private String description;
    private LocalDateTime deadline;
    private List<UserDtoRequest> executors;
    private int priority;
    private Long parentTaskId;
}
