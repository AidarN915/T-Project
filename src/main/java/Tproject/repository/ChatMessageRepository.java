package Tproject.repository;

import Tproject.model.ChatMessage;
import Tproject.model.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
    public Page<ChatMessage> findByChatRoomOrderByCreatedDateDesc(ChatRoom chatRoom, Pageable pageable);
}
