package Tproject.controller;

import Tproject.dto.ProjectDto;
import Tproject.dto.ProjectUpdateDto;
import Tproject.mapper.ProjectMapper;
import Tproject.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    @GetMapping("/getAll")
    public ResponseEntity<List<ProjectDto>> getAll(Authentication auth){
        return ResponseEntity.ok(projectMapper.toListDto(projectService.getAll(auth)));
    }
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDto> getById(@PathVariable Long projectId,Authentication auth){
        return ResponseEntity.ok(projectMapper.toDto(projectService.getById(projectId,auth)));
    }
    @PostMapping("/create")
    public ResponseEntity<ProjectDto> create(Authentication auth,
                                             @RequestParam("title") String title){
        return ResponseEntity.ok(projectMapper.toDto(projectService.create(auth,title)));
    }
    @PostMapping("/update/{projectId}")
    public ResponseEntity<ProjectDto> update(Authentication auth,
                                             @PathVariable Long projectId,
                                             @RequestBody ProjectUpdateDto updateDto){
        return ResponseEntity.ok(projectMapper.toDto(projectService.update(projectId,auth,updateDto)));
    }
    @DeleteMapping("/{projectId}")
    public ResponseEntity<String> delete(@PathVariable Long projectId,
                                         Authentication auth){
        return ResponseEntity.ok(projectService.delete(projectId,auth));
    }
}
