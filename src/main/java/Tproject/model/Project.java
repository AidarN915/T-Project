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

    @OneToMany(mappedBy = "project")
    private List<Board> boards;

    @ManyToMany
    @JoinTable(name = "users_projects",
    joinColumns = @JoinColumn(name = "project_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();
}
