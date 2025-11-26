package Tproject.service;

import Tproject.model.TaskList;
import Tproject.repository.TaskListRepository;
import Tproject.util.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskListServiceImpl implements TaskListService{
    private final TaskListRepository taskListRepository;
    private final UserUtil userUtil;
    @Override
    public TaskList create(String title, HttpServletRequest request) {
        TaskList newList = new TaskList();
        newList.setTitle(title);
        newList.setUser(userUtil.getUserByRequest(request));
        taskListRepository.save(newList);
        return newList;
    }

    @Override
    public List<TaskList> all(HttpServletRequest request) {
        return taskListRepository.getByUser(userUtil.getUserByRequest(request));
    }

    @Override
    public TaskList update(String title, Long taskListId, HttpServletRequest request) {
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Список не найден"));
        if (!taskList.getUser().getId().equals(userUtil.getUserByRequest(request).getId())) {
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
        if (!taskList.getUser().getId().equals(userUtil.getUserByRequest(request).getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        taskListRepository.delete(taskList);
        return "Удалено";
    }
}
