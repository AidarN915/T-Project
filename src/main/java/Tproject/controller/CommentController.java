package Tproject.controller;

import Tproject.dto.CommentDto;
import Tproject.mapper.CommentMapper;
import Tproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @GetMapping("/{taskId}/all")
    public ResponseEntity<List<CommentDto>> getByTask(@PathVariable Long taskId,
                                                      Authentication auth){
        return ResponseEntity.ok(commentMapper.toListDto(commentService.getByTask(taskId,auth)));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getById(@PathVariable Long commentId,Authentication auth){
        return ResponseEntity.ok(commentMapper.toDto(commentService.getById(commentId,auth)));
    }

    @PostMapping("/change-status/{commentId}")
    public ResponseEntity<CommentDto> changeStatus(@PathVariable Long commentId,
                                                   Authentication auth,
                                                   @RequestParam("newStatus") String newStatus){
        return ResponseEntity.ok(commentMapper.toDto(commentService.changeStatus(commentId,newStatus,auth)));
    }
    @PostMapping("/update/{commentId}")
    public ResponseEntity<CommentDto> update(@PathVariable Long commentId,
                                             @RequestParam("newTitle") String newTitle,
                                             Authentication auth){
        return ResponseEntity.ok(commentMapper.toDto(commentService.update(commentId,newTitle,auth)));
    }
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<String> delete(@PathVariable Long commentId,
                                         Authentication auth){
        return ResponseEntity.ok(commentService.delete(commentId,auth));
    }
}
