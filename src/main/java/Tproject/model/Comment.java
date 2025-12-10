package Tproject.model;

import Tproject.enums.CommentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true
,callSuper = false)
@Entity
@Table(name = "comments")
@ToString(onlyExplicitlyIncluded = true)
@Where(clause = "deleted = false")
public class Comment extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String title;
    @Enumerated(EnumType.STRING)
    private CommentStatus status = CommentStatus.INITIATED;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
