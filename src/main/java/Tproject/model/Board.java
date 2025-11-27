package Tproject.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "boards")
@ToString(onlyExplicitlyIncluded = true)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "board")
    private List<TaskList> taskLists;
}
