package Tproject.service;

import Tproject.dto.TaskCreateDto;
import Tproject.model.Task;
import Tproject.model.User;
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
    private final JwtUtil jwtUtil;
    private final UserUtil userUtil;
    @Override
    public Task create(TaskCreateDto createDto,HttpServletRequest request) {
        Task newTask = new Task();
        newTask.setCreator(userUtil.getUserByRequest(request));
        newTask.setExecutor(userRepository.findByUsername(createDto.getExecutor().getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден")));
        newTask.setDeadline(createDto.getDeadline());
        newTask.setDescription(createDto.getDescription());
        newTask.setTitle(createDto.getTitle());
        taskRepository.save(newTask);
        return newTask;
    }


    @Override
    public List<Task> getTasksByRequest(HttpServletRequest request) {
        User user = userUtil.getUserByRequest(request);
        return taskRepository.findByExecutor(user);
    }

    @Override
    public Task update(Long id, TaskCreateDto createDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Задача не найдена"));
        task.setTitle(createDto.getTitle());
        task.setDescription(createDto.getDescription());
        task.setDeadline(createDto.getDeadline());
        task.setExecutor(userRepository.findByUsername(createDto.getExecutor().getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден")));
        taskRepository.save(task);
        return task;
    }

    @Override
    public Task changeStatus(Long id, boolean isDone) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Задача не найдена"));
        if(task.isDone() == isDone){
            return task;
        }
        task.setDone(isDone);
        taskRepository.save(task);
        return task;
    }

    @Override
    public String deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Задача не найдена"));
        taskRepository.delete(task);
        return "Удалено";
    }

    @Override
    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Задача не найдена"));
    }
}
