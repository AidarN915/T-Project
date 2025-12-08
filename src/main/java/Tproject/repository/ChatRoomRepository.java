package Tproject.repository;

import Tproject.model.ChatRoom;
import Tproject.model.Task;
import Tproject.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    @Query("""
    SELECT cr FROM ChatRoom cr
    JOIN cr.task t
    JOIN t.taskList tl
    JOIN tl.board b
    JOIN b.project p
    JOIN p.projectsUsers pu
    WHERE pu.user.id = :userId
      AND (pu.role = 'MODERATOR' OR pu.role = 'EXECUTOR')
""")
    public List<ChatRoom> findChatRoomsAvailableForUser(Long userId);
    public Optional<ChatRoom> findByTask(Task task);
    public Optional<ChatRoom> findByChatRoomKey(String chatRoomKey);
    @Query("SELECT c FROM ChatRoom c " +
            "WHERE c.chatRoomKey LIKE CONCAT(:username, '_%') " +   // username_...
            "OR c.chatRoomKey LIKE CONCAT('%_', :username)")       // ..._username
    List<ChatRoom> findAllByChatRoomKeyContaining(@Param("username") String username);


    @Query("""
    SELECT COUNT(cr) > 0 FROM ChatRoom cr
    JOIN cr.task t
    JOIN t.taskList tl
    JOIN tl.board b
    JOIN b.project p
    JOIN p.projectsUsers pu
    WHERE cr.id = :chatRoomId
      AND pu.user.id = :userId
      AND (pu.role = 'MODERATOR' OR pu.role = 'EXECUTOR')
""")
    public boolean userHasAccessToChat(Long userId, Long chatRoomId);
}
