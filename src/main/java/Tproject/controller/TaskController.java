package Tproject.controller;

import Tproject.dto.TaskCreateDto;
import Tproject.dto.TaskDto;
import Tproject.mapper.TaskMapper;
import Tproject.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequestMapping("/tasks")
@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskMapper taskMapper;
    private final TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getById(@PathVariable Long id, Authentication auth){
        return ResponseEntity.ok(taskMapper.toDto(taskService.getById(id,auth)));
    }
    @GetMapping("/my")
    public ResponseEntity<List<TaskDto>> getMyTasks(Authentication auth){
        return ResponseEntity.ok(taskMapper.toListDto(taskService.getMyTasks(auth)));
    }
    @GetMapping("/{taskListId}/all")
    public ResponseEntity<List<TaskDto>> getMy(@PathVariable Long taskListId,Authentication auth){
        return ResponseEntity.ok(taskMapper.toListDto(taskService.getByTaskListId(taskListId,auth)));
    }
    @PostMapping("/{taskListId}/create")
    public ResponseEntity<TaskDto> create(@PathVariable Long taskListId,
                                          @RequestBody TaskCreateDto createDto,
                                          Authentication auth){
        return ResponseEntity.ok(taskMapper.toDto(taskService.create(taskListId,createDto,auth)));
    }
    @PostMapping("/update/{taskId}")
    public ResponseEntity<TaskDto> update(Authentication auth,
                                        @RequestBody TaskCreateDto createDto,
                                        @PathVariable Long taskId){
        return ResponseEntity.ok(taskMapper.toDto(taskService.update(taskId,createDto,auth)));
    }
    @PostMapping("/change-status/{taskId}")
    public ResponseEntity<TaskDto> changeStatus(@RequestBody boolean isDone,
                                                @PathVariable Long taskId,
                                                Authentication auth){
        return ResponseEntity.ok(taskMapper.toDto(taskService.changeStatus(taskId,isDone,auth)));
    }
    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<String> delete(@PathVariable Long taskId,Authentication auth){
        return ResponseEntity.ok(taskService.deleteTask(taskId,auth));
    }
}
