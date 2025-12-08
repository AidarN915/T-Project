package Tproject.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @ManyToMany
    @JoinTable(name = "tasks_executors",
    joinColumns = @JoinColumn(name = "task_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> executors = new HashSet<>();
    @OneToOne
    @JoinColumn(name = "creator_id")
    private User creator;
    @ManyToOne
    @JoinColumn(name = "list_id")
    private TaskList taskList;
    @OneToMany(mappedBy = "task",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comment> comments;

    @OneToOne(mappedBy = "task",cascade = CascadeType.ALL,orphanRemoval = true)
    private ChatRoom chatRoom;

    @OneToMany(mappedBy = "task",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<TaskImage> taskImages;
}
