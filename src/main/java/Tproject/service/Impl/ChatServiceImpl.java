package Tproject.service.Impl;

import Tproject.enums.MessageType;
import Tproject.enums.OperationType;
import Tproject.mapper.ChatMessageMapper;
import Tproject.model.*;
import Tproject.repository.ChatMessageRepository;
import Tproject.repository.ChatRoomRepository;
import Tproject.repository.TaskRepository;
import Tproject.repository.UserRepository;
import Tproject.security.CustomPermissionEvaluator;
import Tproject.security.Target;
import Tproject.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final CustomPermissionEvaluator permissionEvaluator;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageMapper chatMessageMapper;
    @Override
    public ChatRoom getTaskChatRoom(Long taskId, Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth, Target.task(taskId, OperationType.CHAT))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Задача не найдена"));
        ChatRoom chatRoom = chatRoomRepository.findByTask(task)
                .orElseGet(() -> {
                   ChatRoom newChat = new ChatRoom();
                   newChat.setTask(task);
                   newChat.setType("TASK");
                   chatRoomRepository.save(newChat);
                   return newChat;
                });
        return chatRoom;
    }

    @Override
    public ChatRoom getChatWithUser(String username, Authentication auth) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        if(user.getUsername().equals(auth.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Нельзя создать чат с собой");
        }
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomKey(auth.getName() + "_" + username)
                .orElseGet(() ->
                        chatRoomRepository.findByChatRoomKey(username + "_" + auth.getName())
                                .orElseGet(() -> {
                                    ChatRoom newChat = new ChatRoom();
                                    newChat.setChatRoomKey(auth.getName() + "_" + username);
                                    newChat.setType("USER");
                                    chatRoomRepository.save(newChat);
                                    messagingTemplate.convertAndSend("/topic/user." + user.getId(),
                                            newChat.getId());
                                    return newChat;
                                }));
        sendMessage(chatRoom.getId(),
                "Пользователь " + auth.getName() + " создал чат",
                MessageType.EVENT,
                auth);
        return chatRoom;
    }

    @Override
    public ChatMessage sendMessage(Long chatRoomId, String text, MessageType messageType, Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth,Target.chat(chatRoomId,OperationType.CHAT))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Чат не найден"));
        ChatMessage message = new ChatMessage();
        message.setMessageType(MessageType.MESSAGE);
        message.setText(text);
        message.setSender(userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден")));
        message.setChatRoom(chatRoom);
        chatMessageRepository.save(message);
        messagingTemplate.convertAndSend(
                "/topic/room."+chatRoom.getId(),
                chatMessageMapper.toDto(message));
        return message;
    }

    @Override
    public Page<ChatMessage> getChatMessages(Long chatRoomId, Pageable pageable, Authentication auth) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Чат не найден"));
        return chatMessageRepository.findByChatRoomOrderByCreationDateDesc(chatRoom,pageable);
    }

    @Override
    public List<ChatRoom> getAllMyChats(Authentication auth) {
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        List<ChatRoom> taskChatRooms = chatRoomRepository.findChatRoomsAvailableForUser(user.getId());
        List<ChatRoom> userChatRooms = chatRoomRepository.findAllByChatRoomKeyContaining(user.getUsername());
        List<ChatRoom> allChatRoom = new ArrayList<>(taskChatRooms);
        allChatRoom.addAll(userChatRooms);
        return allChatRoom;
    }
}
