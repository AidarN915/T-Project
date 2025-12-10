package Tproject.service.Impl;

import Tproject.enums.CommentStatus;
import Tproject.enums.OperationType;
import Tproject.model.Comment;
import Tproject.model.Task;
import Tproject.model.User;
import Tproject.repository.CommentRepository;
import Tproject.repository.TaskRepository;
import Tproject.repository.UserRepository;
import Tproject.security.CustomPermissionEvaluator;
import Tproject.security.Target;
import Tproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final CustomPermissionEvaluator permissionEvaluator;
    private final UserRepository userRepository;
    @Override
    public List<Comment> getByTask(Long taskId, Authentication auth) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Задача не найдена"));
        if(!permissionEvaluator.hasAccess(auth, Target.task(taskId, OperationType.READ))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return commentRepository.findByTask(task);
    }

    @Override
    public Comment getById(Long commentId, Authentication auth) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Комментарий не найден"));
        if(!permissionEvaluator.hasAccess(auth,Target.comment(commentId,OperationType.READ))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return comment;
    }

    @Override
    public Comment changeStatus(Long commentId, String newStatus, Authentication auth) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Комментарий не найден"));
        if(!permissionEvaluator.hasAccess(auth,Target.comment(commentId,OperationType.CHANGE_STATUS))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        CommentStatus status;
        try{
            status = CommentStatus.valueOf(newStatus);
        }
        catch (IllegalArgumentException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,ex.getMessage());
        }
        comment.setStatus(status);
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public Comment create(Long taskId, String title, Authentication auth) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Задача не найдена"));
        if(!permissionEvaluator.hasAccess(auth,Target.task(taskId,OperationType.MODIFY))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Comment comment = new Comment();
        comment.setTask(task);
        comment.setTitle(title);
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public Comment update(Long commentId, String newTitle, Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth,Target.comment(commentId,OperationType.MODIFY))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Комментарий не найден"));
        comment.setTitle(newTitle);
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public String delete(Long commentId, Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth,Target.comment(commentId,OperationType.MODIFY))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Комментарий не найден"));
        comment.markAsDeleted();
        commentRepository.save(comment);
        return "Удалено";
    }
}
