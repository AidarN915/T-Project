package Tproject.controller;

import Tproject.dto.ProjectDto;
import Tproject.dto.ProjectUpdateDto;
import Tproject.mapper.ProjectMapper;
import Tproject.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    @GetMapping("/getAll")
    public ResponseEntity<List<ProjectDto>> getAll(HttpServletRequest request){
        return ResponseEntity.ok(projectMapper.toListDto(projectService.getAll(request)));
    }
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDto> getById(@PathVariable Long projectId,HttpServletRequest request){
        return ResponseEntity.ok(projectMapper.toDto(projectService.getById(projectId,request)));
    }
    @PostMapping("/create")
    public ResponseEntity<ProjectDto> create(HttpServletRequest request,
                                             @RequestParam("title") String title){
        return ResponseEntity.ok(projectMapper.toDto(projectService.create(request,title)));
    }
    @PostMapping("/update/{projectId}")
    public ResponseEntity<ProjectDto> update(HttpServletRequest request,
                                             @PathVariable Long projectId,
                                             @RequestBody ProjectUpdateDto updateDto){
        return ResponseEntity.ok(projectMapper.toDto(projectService.update(projectId,request,updateDto)));
    }
    @DeleteMapping("/{projectId}")
    public ResponseEntity<String> delete(@PathVariable Long projectId,
                                         HttpServletRequest request){
        return ResponseEntity.ok(projectService.delete(projectId,request));
    }
}
