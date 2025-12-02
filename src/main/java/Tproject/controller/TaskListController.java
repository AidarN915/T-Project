package Tproject.controller;

import Tproject.dto.TaskListDto;
import Tproject.mapper.TaskListMapper;
import Tproject.service.TaskListService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
                                                        Authentication auth){
        return ResponseEntity.ok(taskListMapper.toListDto(taskListService.getByBoardId(boardId,auth)));
    }

    @PostMapping("/create/{boardId}")
    public ResponseEntity<TaskListDto> create(@PathVariable Long boardId,
                                              Authentication auth,
                                              @RequestParam("title") String title){
        return ResponseEntity.ok(taskListMapper.toDto(taskListService.create(boardId,title,auth)));
    }

    @PostMapping("/update/{taskListId}")
    public ResponseEntity<TaskListDto> update(@RequestParam("newTitle") String newTitle,
                                              Authentication auth,
                                              @PathVariable Long taskListId){
        return ResponseEntity.ok(taskListMapper.toDto(taskListService.update(newTitle,taskListId,auth)));
    }

    @DeleteMapping("/delete/{taskListId}")
    public ResponseEntity<String> delete(@PathVariable Long taskListId,Authentication auth){
        return ResponseEntity.ok(taskListService.delete(taskListId,auth));
    }
}
