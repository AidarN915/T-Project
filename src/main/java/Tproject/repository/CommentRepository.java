package Tproject.repository;

import Tproject.model.Comment;
import Tproject.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    public List<Comment> findByTask(Task task);
}
