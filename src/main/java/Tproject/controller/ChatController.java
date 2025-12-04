package Tproject.controller;

import Tproject.dto.ChatRoomDto;
import Tproject.mapper.ChatMessageMapper;
import Tproject.mapper.ChatRoomMapper;
import Tproject.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final ChatRoomMapper chatRoomMapper;
    private final ChatMessageMapper chatMessageMapper;

/*
    @GetMapping("/task/{taskId}")
    public ResponseEntity<ChatRoomDto> getTaskChatRoom(@PathVariable Long taskId, Authentication auth){
        return ResponseEntity.ok(chatRoomMapper.toDto(chatService.getTaskChatRoom(taskId,auth)));
    }
*/

    @GetMapping("/all")
    public ResponseEntity<List<ChatRoomDto>> getAllMyChats(Authentication auth){
        return ResponseEntity.ok(chatRoomMapper.toListDto(chatService.getAllMyChats(auth)));
    }
    @PostMapping("/createChat")
    public ResponseEntity<ChatRoomDto> createChatWith(@RequestParam("username") String username,Authentication auth){
        return ResponseEntity.ok(chatRoomMapper.toDto(chatService.getChatWithUser(username,auth)));
    }

}
