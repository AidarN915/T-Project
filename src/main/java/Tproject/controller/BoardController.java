package Tproject.controller;

import Tproject.dto.BoardDto;
import Tproject.dto.BoardUpdateDto;
import Tproject.mapper.BoardMapper;
import Tproject.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final BoardMapper boardMapper;

    @GetMapping("/all/{projectId}")
    public ResponseEntity<List<BoardDto>> getAll(Authentication auth, @PathVariable Long projectId){
        return ResponseEntity.ok(boardMapper.toListDto(boardService.getByProjectId(projectId,auth)));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDto> getById(@PathVariable Long boardId, Authentication auth){
        return ResponseEntity.ok(boardMapper.toDto(boardService.getById(boardId,auth)));
    }

    @PostMapping("/create/{projectId}")
    public ResponseEntity<BoardDto> create(@PathVariable Long projectId,
                                           Authentication auth,
                                           @RequestParam("title") String title){
        return ResponseEntity.ok(boardMapper.toDto(boardService.create(projectId,auth,title)));
    }
    @PostMapping("/update/{boardId}")
    public ResponseEntity<BoardDto> update(@PathVariable Long boardId,
                                           @RequestBody BoardUpdateDto updateDto,
                                           Authentication auth){
        return ResponseEntity.ok(boardMapper.toDto(boardService.update(boardId,auth,updateDto)));
    }
    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<String> delete(@PathVariable Long boardId,
                                         Authentication auth){
        return ResponseEntity.ok(boardService.delete(boardId,auth));
    }
}
