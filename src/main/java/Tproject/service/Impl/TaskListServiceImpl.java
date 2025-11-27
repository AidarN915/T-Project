package Tproject.service.Impl;

import Tproject.model.Board;
import Tproject.model.TaskList;
import Tproject.model.User;
import Tproject.repository.BoardRepository;
import Tproject.repository.TaskListRepository;
import Tproject.service.TaskListService;
import Tproject.util.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @Override
    public TaskList create(Long boardId,String title, HttpServletRequest request) {
        TaskList newList = new TaskList();
        newList.setTitle(title);
        if(boardId != 0) {
            newList.setBoard(boardRepository.findById(boardId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Доска не найдена")));
        }else{
            newList.setOwner(userUtil.getUserByRequest(request));
        }
        taskListRepository.save(newList);
        return newList;
    }

    @Override
    public List<TaskList> getByBoardId(Long boardId,HttpServletRequest request) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Доска не найдена"))
                .getTaskLists();

    }

    @Override
    public List<TaskList> all(HttpServletRequest request) {
        User user = userUtil.getUserByRequest(request);
        List<TaskList> listsFromProjects = user.getProjects().stream()
                .flatMap(project -> project.getBoards().stream())
                .flatMap(board -> board.getTaskLists().stream())
                .toList();

        List<TaskList> orphanLists = taskListRepository.findByOwner(user);

        List<TaskList> allLists = new ArrayList<>(listsFromProjects);
        allLists.addAll(orphanLists);
        return allLists;
    }

    @Override
    public TaskList update(String title, Long taskListId, HttpServletRequest request) {
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Список не найден"));
        User user = userUtil.getUserByRequest(request);
        if(taskList.getBoard() != null) {
            if (!taskList.getBoard().getProject().getUsers().contains(user)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }else if(taskList.getOwner() != null){
            if(taskList.getOwner() != user){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }
        /*if((taskList.getOwner() != null && taskList.getOwner() != user) || (taskList.getBoard() != null && !taskList.getBoard().getProject().getUsers().contains(user))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }*/
        if(taskList.getTitle() == title){
            return taskList;
        }
        taskList.setTitle(title);
        taskListRepository.save(taskList);
        return taskList;
    }

    @Override
    public String delete(Long taskListId, HttpServletRequest request) {
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        User user = userUtil.getUserByRequest(request);
        if(taskList.getBoard() != null) {
            if (!taskList.getBoard().getProject().getUsers().contains(user)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }else if(taskList.getOwner() != null){
            if(taskList.getOwner() != user){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }
        taskListRepository.delete(taskList);
        return "Удалено";
    }
}
