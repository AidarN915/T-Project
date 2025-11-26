package Tproject.service;

import Tproject.dto.TaskCreateDto;
import Tproject.model.Task;
import Tproject.model.TaskList;
import Tproject.model.User;
import Tproject.repository.TaskListRepository;
import Tproject.repository.TaskRepository;
import Tproject.repository.UserRepository;
import Tproject.util.JwtUtil;
import Tproject.util.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskListRepository taskListRepository;
    private final JwtUtil jwtUtil;
    private final UserUtil userUtil;
    @Override
    public Task create(Long taskListId,TaskCreateDto createDto,HttpServletRequest request) {
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Список не найден"));
        if (!taskList.getUser().getId().equals(userUtil.getUserByRequest(request).getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Task newTask = new Task();
        newTask.setList(taskList);
        newTask.setDeadline(createDto.getDeadline());
        newTask.setDescription(createDto.getDescription());
        newTask.setTitle(createDto.getTitle());
        taskRepository.save(newTask);
        return newTask;
    }


    @Override
    public List<Task> getByTaskListId(Long taskListId) {
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return taskList.getTasks();
    }

    @Override
    public Task update(Long id, TaskCreateDto createDto,HttpServletRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Задача не найдена"));
        if (!task.getList().getUser().getId().equals(userUtil.getUserByRequest(request).getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        task.setTitle(createDto.getTitle());
        task.setDescription(createDto.getDescription());
        task.setDeadline(createDto.getDeadline());
        taskRepository.save(task);
        return task;
    }

    @Override
    public Task changeStatus(Long id, boolean isDone,HttpServletRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Задача не найдена"));
        if (!task.getList().getUser().getId().equals(userUtil.getUserByRequest(request).getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if(task.isDone() == isDone){
            return task;
        }
        task.setDone(isDone);
        taskRepository.save(task);
        return task;
    }

    @Override
    public String deleteTask(Long id,HttpServletRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Задача не найдена"));
        if (!task.getList().getUser().getId().equals(userUtil.getUserByRequest(request).getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        taskRepository.delete(task);
        return "Удалено";
    }

    @Override
    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Задача не найдена"));
    }
}
