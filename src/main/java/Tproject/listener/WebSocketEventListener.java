package Tproject.listener;

import Tproject.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final OnlineUserService onlineUserService;

    @EventListener
    public void handleConnectEvent(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        if (accessor.getUser() != null) {
            String username = accessor.getUser().getName();
            onlineUserService.addOnline(username);
        }
        if ("/topic/users.online".equals(accessor.getDestination())) {
            onlineUserService.sendOnlineUsers();
        }
    }

    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event) {

        if (event.getUser() != null) {
            String username = event.getUser().getName();
            onlineUserService.removeOnline(username);
            onlineUserService.sendOnlineUsers();
        }
    }
}
