package Tproject.interceptor;

import Tproject.model.ChatRoom;
import Tproject.model.User;
import Tproject.repository.ChatRoomRepository;
import Tproject.repository.UserRepository;
import Tproject.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(
                message, StompHeaderAccessor.class
        );

        assert accessor != null;
        if (StompCommand.CONNECT.equals(accessor.getCommand())
            || StompCommand.SUBSCRIBE.equals(accessor.getCommand())){
            String authToken = accessor.getFirstNativeHeader("Authorization");

            if (authToken != null && authToken.startsWith("Bearer ")) {
                String token = authToken.substring(7);

                if (jwtUtil.validateTokenExpire(token)) {
                    String username = jwtUtil.extractUsername(token);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    username, null, new ArrayList<>()
                            );
                    accessor.setUser(authentication);
                    if(accessor.getDestination() != null && accessor.getDestination().startsWith("/topic/room")) {
                        String destination = accessor.getDestination();
                        assert destination != null;
                        var temp = destination.split("\\.");
                        Long chatRoomId = Long.parseLong(temp[1]);
                        User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new MessagingException("Пользователь не найден"));
                        if (!chatRoomRepository.existsByUsersAndId(user, chatRoomId)) {
                            throw new MessagingException("Доступ запрещён");
                        }
                    }
                    if(accessor.getDestination() != null && accessor.getDestination().startsWith("/topic/user")){
                        String destination = accessor.getDestination();
                        assert destination != null;
                        var temp = destination.split("\\.");
                        Long userId = Long.parseLong(temp[1]);
                        User user = userRepository.findById(userId)
                                .orElseThrow(() -> new MessagingException("Пользователь не найден"));
                        if(!user.getId().equals(userRepository.findByUsername(username)
                                .orElseThrow(() -> new MessagingException("Пользователь не найден")).getId())){
                            throw new MessagingException("Нельзя подписываться на чужой топик");
                        }
                    }
                }
            }
        }
        return message;
    }
}
