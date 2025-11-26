package Tproject.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tasks")
@ToString(onlyExplicitlyIncluded = true)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime created = LocalDateTime.now();
    private LocalDateTime deadline;
    private boolean isDone = false;
    @ManyToOne
    @JoinColumn(name = "executor_id")
    private User executor;
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;
}
