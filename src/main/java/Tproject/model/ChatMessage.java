package Tproject.model;

import Tproject.enums.MessageType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true,
callSuper = false)
@Table(name = "chat_messages")
@ToString(onlyExplicitlyIncluded = true)
@Where(clause = "deleted = false")
public class ChatMessage extends AuditableEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String text;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;
}
