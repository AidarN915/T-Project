package Tproject.repository;

import Tproject.model.ChatRoom;
import Tproject.model.Task;
import Tproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    public List<ChatRoom> findByUsers(User user);
    public Optional<ChatRoom> findByTask(Task task);
    public Optional<ChatRoom> findByChatRoomKey(String chatRoomKey);
    public boolean existsByUserAndId(User user,Long id);
}
