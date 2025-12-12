package Tproject.service.Impl;

import Tproject.dto.ChatMessageDto;
import Tproject.dto.TaskCreateDto;
import Tproject.dto.UserDto;
import Tproject.dto.UserDtoRequest;
import Tproject.enums.MessageType;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
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
    @Transactional
    public Task create(Long taskListId, TaskCreateDto createDto, Authentication auth) {
        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Список не найден"));
        if(!permissionEvaluator.hasAccess(auth, Target.taskList(taskListId, OperationType.MODIFY))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Task newTask = new Task();
        newTask.setTaskList(taskList);
        if(createDto.getExecutors() != null && !createDto.getExecutors().isEmpty()) {
            for(UserDtoRequest executorDto : createDto.getExecutors()){
                User executor = userRepository.findByUsername(executorDto.getUsername())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Исполнитель не найден"));
                newTask.getExecutors().add(executor);
            }
        }
        newTask.setDeadline(createDto.getDeadline());
        newTask.setDescription(createDto.getDescription());
        newTask.setTitle(createDto.getTitle());
        newTask.setPriority(createDto.getPriority());
        if(createDto.getParentTaskId() != null){
            Task parent = taskRepository.findById(createDto.getParentTaskId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Родительская задача не найдена"));
            newTask.setParentTask(parent);
        }
        taskRepository.save(newTask);

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setTask(newTask);
        chatRoom.setType("TASK");
        chatRoomRepository.save(chatRoom);
        chatService.sendMessage(chatRoom.getId(),
                "Пользователь " + auth.getName() + " создал задачу",
                MessageType.EVENT,
                auth);
        for(ProjectsUsers userForNotify:chatRoom.getTask().getTaskList().getBoard().getProject().getProjectsUsers()){
            messagingTemplate.convertAndSend("/topic/user." + userForNotify.getUser().getId(),
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
        task.getExecutors().clear();
        if(createDto.getExecutors() != null && !createDto.getExecutors().isEmpty()) {
            for(UserDtoRequest executorDto:createDto.getExecutors()){
                User executor = userRepository.findByUsername(executorDto.getUsername())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Исполнитель не найден"));
                task.getExecutors().add(executor);
            }
        }
        task.setTitle(createDto.getTitle());
        task.setPriority(createDto.getPriority());
        task.setDescription(createDto.getDescription());
        task.setDeadline(createDto.getDeadline());

        if(createDto.getParentTaskId() != null){
            Task parent = taskRepository.findById(createDto.getParentTaskId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Родительская задача не найдена"));
            task.setParentTask(parent);
        }
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
       task.markAsDeleted();
       task.getChatRoom().markAsDeleted();
       taskRepository.save(task);
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
        return taskRepository.findByExecutors(user);
    }
}
