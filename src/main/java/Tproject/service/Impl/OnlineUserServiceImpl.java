package Tproject.service.Impl;

import Tproject.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OnlineUserServiceImpl implements OnlineUserService {
    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();
    private final SimpMessagingTemplate messagingTemplate;
    @Override
    public void addOnline(String username) {
        onlineUsers.add(username);
        sendOnlineUsers();
    }

    @Override
    public void removeOnline(String username) {
        onlineUsers.remove(username);
        sendOnlineUsers();
    }

    @Override
    public boolean isOnline(String username) {
        return onlineUsers.contains(username);
    }

    @Override
    public void sendOnlineUsers() {
        messagingTemplate.convertAndSend("/topic/users.online",
                onlineUsers);
    }
}
