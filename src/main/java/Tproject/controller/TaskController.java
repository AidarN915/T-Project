package Tproject.controller;

import Tproject.dto.TaskCreateDto;
import Tproject.dto.TaskDto;
import Tproject.mapper.TaskMapper;
import Tproject.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/tasks")
@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskMapper taskMapper;
    private final TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(taskMapper.toDto(taskService.getById(id)));
    }
    @GetMapping("/my")
    public ResponseEntity<List<TaskDto>> getMy(HttpServletRequest request){
        return ResponseEntity.ok(taskMapper.toListDto(taskService.getTasksByRequest(request)));
    }
    @PostMapping("/create")
    public ResponseEntity<TaskDto> create(@RequestBody TaskCreateDto createDto,HttpServletRequest request){
        return ResponseEntity.ok(taskMapper.toDto(taskService.create(createDto,request)));
    }
    @PostMapping("/update/{taskId}")
    public ResponseEntity<TaskDto> update(@RequestBody TaskCreateDto createDto,@PathVariable Long taskId){
        return ResponseEntity.ok(taskMapper.toDto(taskService.update(taskId,createDto)));
    }
    @PostMapping("/change-status/{taskId}")
    public ResponseEntity<TaskDto> changeStatus(@RequestBody boolean isDone, @PathVariable Long taskId){
        return ResponseEntity.ok(taskMapper.toDto(taskService.changeStatus(taskId,isDone)));
    }
    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<String> delete(@PathVariable Long taskId){
        return ResponseEntity.ok(taskService.deleteTask(taskId));
    }
}
