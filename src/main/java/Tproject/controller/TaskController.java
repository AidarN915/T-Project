package Tproject.controller;

import Tproject.dto.TaskCreateDto;
import Tproject.model.Task;
import Tproject.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/tasks")
@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskRepository taskRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable Long id){
        return ResponseEntity.ok(taskRepository.getById(id));
    }
    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAll(){
        return ResponseEntity.ok(taskRepository.findAll());
    }
    @PostMapping("/create")
    public ResponseEntity<Task> create(@RequestBody TaskCreateDto createDto){
        return null;
    }
    @PostMapping("/update/{taskId}")
    public ResponseEntity<Task> update(@RequestBody TaskCreateDto createDto,@PathVariable Long taskId){
        return null;
    }
    @PostMapping("/change-status/{taskId}")
    public ResponseEntity<Task> changeStatus(@RequestBody boolean isDone,@PathVariable Long taskId){
        return null;
    }
    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<String> delete(@PathVariable Long taskId){
        return null;
    }
}
