package Tproject.controller;

import Tproject.dto.TaskCreateDto;
import Tproject.dto.TaskDto;
import Tproject.mapper.TaskMapper;
import Tproject.model.Task;
import Tproject.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequestMapping("/tasks")
@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(taskMapper.toDto(taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользоотваель не найден"))));
    }
    @GetMapping("/all")
    public ResponseEntity<List<TaskDto>> getAll(){
        return ResponseEntity.ok(taskMapper.toListDto(taskRepository.findAll()));
    }
    @PostMapping("/create")
    public ResponseEntity<TaskDto> create(@RequestBody TaskCreateDto createDto){
        return null;
    }
    @PostMapping("/update/{taskId}")
    public ResponseEntity<TaskDto> update(@RequestBody TaskCreateDto createDto,@PathVariable Long taskId){
        return null;
    }
    @PostMapping("/change-status/{taskId}")
    public ResponseEntity<TaskDto> changeStatus(@RequestBody boolean isDone, @PathVariable Long taskId){
        return null;
    }
    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<String> delete(@PathVariable Long taskId){
        return null;
    }
}
