package Tproject.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@Table(name = "task_uploads")
@EqualsAndHashCode(onlyExplicitlyIncluded = true,
callSuper = false)
@Where(clause = "deleted = false")
public class TaskUpload extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String url;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
