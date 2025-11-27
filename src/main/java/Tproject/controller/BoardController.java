package Tproject.controller;

import Tproject.dto.BoardDto;
import Tproject.dto.BoardUpdateDto;
import Tproject.mapper.BoardMapper;
import Tproject.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final BoardMapper boardMapper;

    @GetMapping("/all/{projectId}")
    public ResponseEntity<List<BoardDto>> getAll(HttpServletRequest request, @PathVariable Long projectId){
        return ResponseEntity.ok(boardMapper.toListDto(boardService.getByProjectId(projectId,request)));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDto> getById(@PathVariable Long boardId,HttpServletRequest request){
        return ResponseEntity.ok(boardMapper.toDto(boardService.getById(boardId,request)));
    }

    @PostMapping("/create/{projectId}")
    public ResponseEntity<BoardDto> create(@PathVariable Long projectId,
                                           HttpServletRequest request,
                                           @RequestParam("title") String title){
        return ResponseEntity.ok(boardMapper.toDto(boardService.create(projectId,request,title)));
    }
    @PostMapping("/update/{boardId}")
    public ResponseEntity<BoardDto> update(@PathVariable Long boardId,
                                           @RequestBody BoardUpdateDto updateDto,
                                           HttpServletRequest request){
        return ResponseEntity.ok(boardMapper.toDto(boardService.update(boardId,request,updateDto)));
    }
    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<String> delete(@PathVariable Long boardId,
                                         HttpServletRequest request){
        return ResponseEntity.ok(boardService.delete(boardId,request));
    }
}
