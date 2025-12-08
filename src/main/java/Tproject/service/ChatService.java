package Tproject.service;

import Tproject.enums.MessageType;
import Tproject.model.ChatMessage;
import Tproject.model.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ChatService {
    public ChatRoom getTaskChatRoom(Long taskId, Authentication auth);
    public ChatRoom getChatWithUser(String username,Authentication auth);
    public ChatMessage sendMessage(Long chatRoomId, String text, MessageType messageType, Authentication auth);
    public Page<ChatMessage> getChatMessages(Long chatRoomId, Pageable pageable,Authentication auth);
    public List<ChatRoom> getAllMyChats(Authentication auth);
}
