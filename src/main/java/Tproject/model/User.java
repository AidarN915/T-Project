package Tproject.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "users")
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true,nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role = "USER";
    private String refreshToken;
    private LocalDateTime refreshTokenExpires;
    private String phoneNumber;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<ProjectsUsers> projectsUsers = new ArrayList<>();

    private String avatarUrl;
}
