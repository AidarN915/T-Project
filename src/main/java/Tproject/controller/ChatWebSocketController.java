package Tproject.controller;

import Tproject.dto.ws.ChatHistoryRequest;
import Tproject.dto.ChatMessageDto;
import Tproject.dto.ws.SendMessageRequest;
import Tproject.dto.ws.TypingDto;
import Tproject.enums.MessageType;
import Tproject.mapper.ChatMessageMapper;
import Tproject.mapper.ChatRoomMapper;
import Tproject.model.ChatMessage;
import Tproject.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        chatService.sendMessage(request.getChatRoomId(),request.getText(), MessageType.MESSAGE,auth);
    }

    @MessageMapping("/chat.history")
    public void getChatHistory(
            @Payload ChatHistoryRequest request,
            Authentication auth){
        Pageable pageable = PageRequest.of(request.getPage(),request.getSize());
        Page<ChatMessage> messages = chatService.getChatMessages(request.getChatRoomId(),pageable,auth);
        Page<ChatMessageDto> messageDto = messages.map(chatMessageMapper::toDto);
        messagingTemplate.convertAndSend(
                "/topic/room." + request.getChatRoomId() + ".history",
                messageDto
        );
    }
    @MessageMapping("/chat.typing")
    public void typing(
            @Payload TypingDto request,
            Authentication auth){
        request.setUsername(auth.getName());
        messagingTemplate.convertAndSend(
                "/topic/room." + request.getChatRoomId() + ".typing",
                request
        );
    }
}
