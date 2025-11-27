package Tproject.controller;

import Tproject.dto.TaskListDto;
import Tproject.mapper.TaskListMapper;
import Tproject.service.TaskListService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/task-list")
@RestController
@RequiredArgsConstructor
public class TaskListController {
    private final TaskListService taskListService;
    private final TaskListMapper taskListMapper;

    @GetMapping("/{boardId}/all")
    public ResponseEntity<List<TaskListDto>> getByBoardId(@PathVariable Long boardId,
                                                        HttpServletRequest request){
        return ResponseEntity.ok(taskListMapper.toListDto(taskListService.getByBoardId(boardId,request)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskListDto>> getAll(HttpServletRequest request){
        return ResponseEntity.ok(taskListMapper.toListDto(taskListService.all(request)));
    }

    @PostMapping("/create/{boardId}")
    public ResponseEntity<TaskListDto> create(@PathVariable Long boardId,
                                              HttpServletRequest request,
                                              @RequestParam("title") String title){
        return ResponseEntity.ok(taskListMapper.toDto(taskListService.create(boardId,title,request)));
    }

    @PostMapping("/update/{taskListId}")
    public ResponseEntity<TaskListDto> update(@RequestParam("newTitle") String newTitle,
                                              HttpServletRequest request,
                                              @PathVariable Long taskListId){
        return ResponseEntity.ok(taskListMapper.toDto(taskListService.update(newTitle,taskListId,request)));
    }

    @DeleteMapping("/delete/{taskListId}")
    public ResponseEntity<String> delete(@PathVariable Long taskListId,HttpServletRequest request){
        return ResponseEntity.ok(taskListService.delete(taskListId,request));
    }
}
