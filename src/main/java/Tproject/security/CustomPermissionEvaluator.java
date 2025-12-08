package Tproject.security;

import Tproject.enums.ObjectType;
import Tproject.enums.OperationType;
import Tproject.enums.UserProjectRoles;
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
    private final ProjectsUsersRepository projectsUsersRepository;
    private final CommentRepository commentRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final TaskImageRepository taskImageRepository;

    public boolean hasAccess(Authentication auth, TargetIdentifier target) {
        if (auth == null || !auth.isAuthenticated() || target == null) {
            return false;
        }

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if ("SUPERADMIN".equals(user.getRole()) || "ADMIN".equals(user.getRole())) {
            return true;
        }

        Project project;
        switch (target.type()){
            case PROJECT:
                project = projectRepository.findById(target.id())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Проект не найден"));
                break;
            case BOARD:
                project = boardRepository.findById(target.id())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Доска не найдена"))
                        .getProject();
                break;
            case TASK_LIST:
                project = taskListRepository.findById(target.id())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Проект не найден"))
                        .getBoard()
                        .getProject();
                break;
            case TASK:
                project = taskRepository.findById(target.id())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Проект не найден"))
                        .getTaskList()
                        .getBoard()
                        .getProject();
                break;
            case COMMENT:
                project = commentRepository.findById(target.id())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Проект не найден"))
                        .getTask()
                        .getTaskList()
                        .getBoard()
                        .getProject();
                break;
            case CHAT:
                ChatRoom chatRoom = chatRoomRepository.findById(target.id())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Проект не найден"));
                if(chatRoom.getTask() == null){
                    return chatRoom.getChatRoomKey().contains(user.getUsername());
                }
                project = chatRoom.getTask()
                        .getTaskList()
                        .getBoard()
                        .getProject();
                break;
            case TASKIMAGE:
                project = taskImageRepository.findById(target.id())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Проект не найден"))
                        .getTask()
                        .getTaskList()
                        .getBoard()
                        .getProject();
                break;
            default:
                return false;
        }

        return switch (target.opType()) {
            case OperationType.READ -> projectsUsersRepository.existsByUserAndProject(user,project);
            case OperationType.CHANGE_STATUS -> (projectsUsersRepository.existsByUserAndProjectAndRole(user,project, UserProjectRoles.EXECUTOR) || projectsUsersRepository.existsByUserAndProjectAndRole(user,project, UserProjectRoles.MODERATOR));
            case OperationType.MODIFY -> projectsUsersRepository.existsByUserAndProjectAndRole(user,project, UserProjectRoles.MODERATOR);
            default -> false;
        };
    }


}