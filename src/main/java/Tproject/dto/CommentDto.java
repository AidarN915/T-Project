package Tproject.dto;

import Tproject.enums.CommentStatus;
import Tproject.model.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private String title;
    private UserDto commentator;
    private LocalDateTime creationDate;
    private CommentStatus status;
}
