package Tproject.controller;

import Tproject.dto.TaskImageDto;
import Tproject.mapper.TaskImageMapper;
import Tproject.service.TaskImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/task-images")
@RequiredArgsConstructor
public class TaskImageController {
    private final TaskImageService taskImageService;
    private final TaskImageMapper taskImageMapper;
    @GetMapping("/{id}")
    public ResponseEntity<TaskImageDto> getById(@PathVariable Long id, Authentication auth){
        return ResponseEntity.ok(taskImageMapper.toDto(taskImageService.getById(id,auth)));
    }

    @GetMapping("/{taskId}/all")
    public ResponseEntity<List<TaskImageDto>> getByTaskId(@PathVariable Long taskId,Authentication auth){
        return ResponseEntity.ok(taskImageMapper.toListDto(taskImageService.getByTask(taskId,auth)));
    }

    @PostMapping(value = "/upload/{taskId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TaskImageDto> upload(@PathVariable Long taskId,
                                               Authentication auth,
                                               @RequestParam("file") MultipartFile file){
        return ResponseEntity.ok(taskImageMapper.toDto(taskImageService.upload(taskId,file,auth)));
    }

    @DeleteMapping("/delete/{taskImageId}")
    public ResponseEntity<String> delete(@PathVariable Long taskImageid,
                                         Authentication auth){
        return ResponseEntity.ok(taskImageService.delete(taskImageid,auth));
    }
}
