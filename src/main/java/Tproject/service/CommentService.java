package Tproject.service;

import Tproject.model.Comment;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CommentService {
    public List<Comment> getByTask(Long taskId, Authentication auth);
    public Comment getById(Long commentId,Authentication auth);
    public Comment changeStatus(Long commentId,String newStatus,Authentication auth);
    public Comment create(Long taskId,String title,Authentication auth);
    public Comment update(Long commentId,String newTitle,Authentication auth);
    public String delete(Long commentId,Authentication auth);
}
