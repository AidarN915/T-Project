package Tproject.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@Table(name = "task_list")
@ToString(onlyExplicitlyIncluded = true)
public class TaskList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "taskList",cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
}
