package Tproject.service.Impl;

import Tproject.enums.MessageType;
import Tproject.enums.OperationType;
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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Pageable;
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
    @Override
    public ChatRoom getTaskChatRoom(Long taskId, Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth, Target.task(taskId, OperationType.READ))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Задача не найдена"));
        ChatRoom chatRoom = chatRoomRepository.findByTask(task)
                .orElseGet(() -> {
                   ChatRoom newChat = new ChatRoom();
                   newChat.setTask(task);
                   newChat.setType("TASK");
                   newChat.setUsers(task.getTaskList().getBoard().
                           getProject().getProjectsUsers()
                           .stream().map(ProjectsUsers::getUser)
                           .collect(Collectors.toSet()));
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
                                    Set<User> set = new HashSet<>();
                                    set.add(userRepository.findByUsername(username)
                                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден")));
                                    set.add(userRepository.findByUsername(auth.getName())
                                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден")));
                                    newChat.setUsers(new HashSet<>(set));
                                    chatRoomRepository.save(newChat);
                                    return newChat;
                                }));
        return chatRoom;
    }

    @Override
    public ChatMessage sendMessage(Long chatRoomId, String text, Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth,Target.chat(chatRoomId,OperationType.MODIFY))){
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
        return chatRoomRepository.findByUsers(user);
    }
}
