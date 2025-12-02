package Tproject.service.Impl;

import Tproject.enums.OperationType;
import Tproject.model.Board;
import Tproject.model.Project;
import Tproject.model.TaskList;
import Tproject.model.User;
import Tproject.repository.BoardRepository;
import Tproject.repository.ProjectRepository;
import Tproject.repository.TaskListRepository;
import Tproject.repository.UserRepository;
import Tproject.security.CustomPermissionEvaluator;
import Tproject.security.Target;
import Tproject.service.TaskListService;
import Tproject.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskListServiceImpl implements TaskListService {
    private final TaskListRepository taskListRepository;
    private final UserUtil userUtil;
    private final BoardRepository boardRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CustomPermissionEvaluator permissionEvaluator;
    @Override
    public TaskList create(Long boardId,String title,  Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth, Target.board(boardId, OperationType.MODIFY))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        TaskList newList = new TaskList();
        newList.setTitle(title);
        newList.setBoard(boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Доска не найдена")));
        taskListRepository.save(newList);
        return newList;
    }

    @Override
    public List<TaskList> getByBoardId(Long boardId, Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth,Target.board(boardId,OperationType.READ))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Доска не найдена"))
                .getTaskLists();

    }

    @Override
    public TaskList update(String title, Long taskListId,  Authentication auth) {
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Список не найден"));
        if(!permissionEvaluator.hasAccess(auth,Target.taskList(taskListId,OperationType.MODIFY))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if(taskList.getTitle() == title){
            return taskList;
        }
        taskList.setTitle(title);
        taskListRepository.save(taskList);
        return taskList;
    }

    @Override
    public String delete(Long taskListId,  Authentication auth) {
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(!permissionEvaluator.hasAccess(auth,Target.taskList(taskListId,OperationType.MODIFY))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        taskListRepository.delete(taskList);
        return "Удалено";
    }
}
