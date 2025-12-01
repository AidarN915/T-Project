package Tproject.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "projects")
@ToString(onlyExplicitlyIncluded = true)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "project",cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<Board> boards;

    @ManyToMany
    @JoinTable(name = "viewers_projects",
    joinColumns = @JoinColumn(name = "project_id"),
    inverseJoinColumns = @JoinColumn(name = "viewer_id"))
    private Set<User> viewers = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "executors_projects",
    joinColumns = @JoinColumn(name = "project_id"),
    inverseJoinColumns = @JoinColumn(name = "executor_id"))
    private Set<User> executors = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "moderators_projects",
    joinColumns = @JoinColumn(name = "project_id"),
    inverseJoinColumns = @JoinColumn(name = "moderator_id"))
    private Set<User> moderators = new HashSet<>();
}
