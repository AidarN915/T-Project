package Tproject.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true
,callSuper = false)
@Entity
@Table(name = "projects")
@ToString(onlyExplicitlyIncluded = true)
@Where(clause = "deleted = false")
public class Project extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProjectsUsers> projectsUsers = new ArrayList<>();
}
