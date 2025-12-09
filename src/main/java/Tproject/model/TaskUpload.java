package Tproject.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "task_uploads")
@ToString(onlyExplicitlyIncluded = true)
public class TaskUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
