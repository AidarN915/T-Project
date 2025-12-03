package Tproject.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "chat_rooms")
@ToString(onlyExplicitlyIncluded = true)
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;//"TASK" or "USER"

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    private String chatRoomKey;

    @ManyToMany
    @JoinTable(
            name = "users_chatrooms",
            joinColumns = @JoinColumn(name = "chatroom_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages = new ArrayList<>();

}
