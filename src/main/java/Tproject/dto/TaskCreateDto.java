package Tproject.dto;

import Tproject.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskCreateDto {
    private String title;
    private String description;
    private LocalDateTime deadline;
    private UserDto executor;
}
