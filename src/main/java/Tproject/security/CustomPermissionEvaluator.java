package Tproject.security;

import Tproject.enums.ObjectType;
import Tproject.enums.OperationType;
import Tproject.model.*;
import Tproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Component("permissionEvaluator")
@RequiredArgsConstructor
public class CustomPermissionEvaluator{

    private final ProjectRepository projectRepository;
    private final BoardRepository boardRepository;
    private final TaskListRepository taskListRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public boolean hasAccess(Authentication auth, TargetIdentifier target) {
        if (auth == null || !auth.isAuthenticated() || target == null) {
            return false;
        }

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if ("SUPERADMIN".equals(user.getRole()) || "ADMIN".equals(user.getRole())) {
            return true;
        }

        return switch (target.type()) {
            case PROJECT -> hasProjectAccess(user, target.id(),target.opType());
            case BOARD -> hasBoardAccess(user, target.id(),target.opType());
            case TASK_LIST -> hasTaskListAccess(user, target.id(),target.opType());
            case TASK -> hasTaskAccess(user, target.id(),target.opType());
            default -> false;
        };
    }

    private boolean hasProjectAccess(User user, Long projectId, OperationType op) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        return switch (op) {
            case OperationType.READ -> project.getViewers().contains(user);
            case OperationType.CHANGE_STATUS -> project.getExecutors().contains(user);
            case OperationType.MODIFY -> project.getModerators().contains(user);
            default -> false;
        };
    }

    private boolean hasBoardAccess(User user, Long boardId, OperationType op) {
        Board board;
        if(boardId != 0) {
            board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));
        }else {
            return true;
        }
        return switch (op) {
            case OperationType.READ -> board.getProject().getViewers().contains(user);
            case OperationType.CHANGE_STATUS -> board.getProject().getExecutors().contains(user);
            case OperationType.MODIFY -> board.getProject().getModerators().contains(user);
            default -> false;
        };
    }

    private boolean hasTaskListAccess(User user, Long taskListId, OperationType op) {
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TaskList not found"));

        if (taskList.getBoard() != null) {
            return switch (op) {
                case OperationType.READ -> taskList.getBoard().getProject().getViewers().contains(user);
                case OperationType.CHANGE_STATUS ->  taskList.getBoard().getProject().getExecutors().contains(user);
                case OperationType.MODIFY ->  taskList.getBoard().getProject().getModerators().contains(user);
                default -> false;
            };
        } else {
            return user.equals(taskList.getOwner());
        }
    }

    private boolean hasTaskAccess(User user, Long taskId, OperationType op) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        if (task.getTaskList().getBoard() != null) {
            return switch (op) {
                case OperationType.READ -> task.getTaskList().getBoard().getProject().getViewers().contains(user);
                case OperationType.CHANGE_STATUS -> task.getTaskList().getBoard().getProject().getExecutors().contains(user);
                case OperationType.MODIFY -> task.getTaskList().getBoard().getProject().getModerators().contains(user);
                default -> false;
            };
        } else {
            Set<User> authorizedUsers = new HashSet<>();
            authorizedUsers.add(task.getCreator());
            if (task.getExecutor() != null) {
                authorizedUsers.add(task.getExecutor());
            }
            authorizedUsers.add(task.getTaskList().getOwner());
            return authorizedUsers.contains(user);
        }
    }

}