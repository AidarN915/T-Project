package Tproject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true,
callSuper = false)
@Table(name = "tasks")
@ToString(onlyExplicitlyIncluded = true)
@Where(clause = "deleted = false")
public class Task extends AuditableEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String title;
    private String description;
    private LocalDateTime deadline;
    private boolean isDone = false;

    @Column(nullable = false)
    @Min(0)
    @Max(4)
    private int priority;

    @ManyToMany
    @JoinTable(name = "tasks_executors",
    joinColumns = @JoinColumn(name = "task_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> executors = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "list_id")
    private TaskList taskList;

    @OneToMany(mappedBy = "task",cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToOne(mappedBy = "task",cascade = CascadeType.ALL)
    private ChatRoom chatRoom;

    @OneToMany(mappedBy = "task",cascade = CascadeType.ALL)
    private List<TaskUpload> taskUploads;

    @ManyToOne
    @JoinColumn(name = "parent_task_id")
    private Task parentTask;

    @OneToMany(mappedBy = "parentTask",cascade = CascadeType.ALL)
    private List<Task> subtasks = new ArrayList<>();
}
