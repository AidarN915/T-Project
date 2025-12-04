package Tproject.service.Impl;

import Tproject.dto.ChatMessageDto;
import Tproject.dto.TaskCreateDto;
import Tproject.enums.OperationType;
import Tproject.model.*;
import Tproject.repository.ChatRoomRepository;
import Tproject.repository.TaskListRepository;
import Tproject.repository.TaskRepository;
import Tproject.repository.UserRepository;
import Tproject.security.CustomPermissionEvaluator;
import Tproject.security.Target;
import Tproject.service.ChatService;
import Tproject.service.TaskService;
import Tproject.util.JwtUtil;
import Tproject.util.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskListRepository taskListRepository;
    private final JwtUtil jwtUtil;
    private final UserUtil userUtil;
    private final CustomPermissionEvaluator permissionEvaluator;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;
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

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setTask(newTask);
        chatRoom.setType("TASK");
        chatRoom.setUsers(newTask.getTaskList().getBoard().
                getProject().getProjectsUsers()
                .stream().map(ProjectsUsers::getUser)
                .collect(Collectors.toSet()));
        chatRoomRepository.save(chatRoom);
        chatService.sendEventMessage(chatRoom.getId(),
                "Пользователь " + auth.getName() + " создал задачу",
                auth);
        for(User userForNotify:chatRoom.getUsers()){
            messagingTemplate.convertAndSend("/topic/user." + userForNotify.getId(),
                    chatRoom.getId());
        }
        newTask.setChatRoom(chatRoom);
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

        messagingTemplate.convertAndSend(
                "/topic/room."+chatRoomRepository.findByTask(task),
                "task status changed to " + isDone
        );
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
    @Override
    public List<Task> getMyTasks(Authentication auth) {
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        return taskRepository.findByExecutor(user);
    }
}
