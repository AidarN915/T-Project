package Tproject.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@ToString(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role = "USER";
    private String refreshToken;
    private LocalDateTime refreshTokenExpires;

    @OneToMany(mappedBy = "executor")
    private List<Task> tasks;
    @OneToMany(mappedBy = "creator")
    private List<Task> createdTasks;
}
