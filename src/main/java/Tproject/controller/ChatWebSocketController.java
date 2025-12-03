package Tproject.controller;

import Tproject.dto.SendMessageRequest;
import Tproject.mapper.ChatMessageMapper;
import Tproject.mapper.ChatRoomMapper;
import Tproject.model.ChatMessage;
import Tproject.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {
    private final ChatService chatService;
    private final ChatRoomMapper chatRoomMapper;
    private final ChatMessageMapper chatMessageMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(
            @Payload SendMessageRequest request,
            Authentication auth){
        ChatMessage message = chatService.sendMessage(request.getChatRoomId(),request.getText(),auth);
        messagingTemplate.convertAndSend(
                "/topic/room." + message.getChatRoom().getId(),
                chatMessageMapper.toDto(message)
        );
    }
}
