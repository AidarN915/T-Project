package Tproject.service.Impl;

import Tproject.model.TaskList;
import Tproject.repository.BoardRepository;
import Tproject.repository.TaskListRepository;
import Tproject.service.TaskListService;
import Tproject.util.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        newList.setBoard(boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Доска не найдена")));
        taskListRepository.save(newList);
        return newList;
    }

    @Override
    public List<TaskList> all(Long boardId,HttpServletRequest request) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Доска не найдена"))
                .getTaskLists();

    }

    @Override
    public TaskList update(String title, Long taskListId, HttpServletRequest request) {
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Список не найден"));
        if (!taskList.getBoard().getProject().getUsers().contains(userUtil.getUserByRequest(request))) {
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
    public String delete(Long taskListId, HttpServletRequest request) {
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!taskList.getBoard().getProject().getUsers().contains(userUtil.getUserByRequest(request))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        taskListRepository.delete(taskList);
        return "Удалено";
    }
}
