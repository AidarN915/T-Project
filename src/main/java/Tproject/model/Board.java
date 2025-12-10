package Tproject.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "boards")
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true,
callSuper = false)
@Where(clause = "deleted = false")
public class Board extends AuditableEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "board",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<TaskList> taskLists;
}
