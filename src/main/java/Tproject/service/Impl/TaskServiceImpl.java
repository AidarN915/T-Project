package Tproject.service.Impl;

import Tproject.dto.TaskCreateDto;
import Tproject.enums.OperationType;
import Tproject.model.Task;
import Tproject.model.TaskList;
import Tproject.model.User;
import Tproject.repository.TaskListRepository;
import Tproject.repository.TaskRepository;
import Tproject.repository.UserRepository;
import Tproject.security.CustomPermissionEvaluator;
import Tproject.security.Target;
import Tproject.service.TaskService;
import Tproject.util.JwtUtil;
import Tproject.util.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskListRepository taskListRepository;
    private final JwtUtil jwtUtil;
    private final UserUtil userUtil;
    private final CustomPermissionEvaluator permissionEvaluator;
    @Override
    public Task create(Long taskListId, TaskCreateDto createDto, Authentication auth) {
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Список не найден"));
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        if(!permissionEvaluator.hasAccess(auth, Target.taskList(taskListId, OperationType.MODIFY))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Task newTask = new Task();
        newTask.setTaskList(taskList);
        if(createDto.getExecutor() != null) {
            newTask.setExecutor(userRepository.findByUsername(createDto.getExecutor().getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Исполнитель не найден")));
        }
        newTask.setCreator(user);
        newTask.setDeadline(createDto.getDeadline());
        newTask.setDescription(createDto.getDescription());
        newTask.setTitle(createDto.getTitle());
        taskRepository.save(newTask);
        return newTask;
    }


    @Override
    public List<Task> getByTaskListId(Long taskListId,Authentication auth) {
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!permissionEvaluator.hasAccess(auth, Target.taskList(taskListId,OperationType.READ))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return taskList.getTasks();
    }

    @Override
    public Task update(Long id, TaskCreateDto createDto,Authentication auth) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Задача не найдена"));
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        if(!permissionEvaluator.hasAccess(auth,Target.task(id,OperationType.MODIFY))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if(createDto.getExecutor() != null) {
            User executor = userRepository.findByUsername(createDto.getExecutor().getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Исполнитель не найден"));
            task.setExecutor(executor);
        }else{
            task.setExecutor(null);
        }
        task.setTitle(createDto.getTitle());
        task.setDescription(createDto.getDescription());
        task.setDeadline(createDto.getDeadline());
        taskRepository.save(task);
        return task;
    }

    @Override
    public Task changeStatus(Long id, boolean isDone,Authentication auth) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Задача не найдена"));
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        if(!permissionEvaluator.hasAccess(auth,Target.task(id,OperationType.CHANGE_STATUS))){
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
    public String deleteTask(Long id,Authentication auth) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Задача не найдена"));
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
       if(!permissionEvaluator.hasAccess(auth,Target.task(id,OperationType.MODIFY))){
           throw new ResponseStatusException(HttpStatus.FORBIDDEN);
       }
        taskRepository.delete(task);
        return "Удалено";
    }

    @Override
    public Task getById(Long id,Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth,Target.task(id,OperationType.READ))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Задача не найдена"));
    }
}
