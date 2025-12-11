package Tproject.dto;

import Tproject.enums.CommentStatus;
import Tproject.model.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto  extends AuditableDto {
    private Long id;
    private String title;
    private CommentStatus status;
}
