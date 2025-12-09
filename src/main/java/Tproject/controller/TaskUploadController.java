package Tproject.controller;

import Tproject.dto.TaskUploadDto;
import Tproject.mapper.TaskUploadMapper;
import Tproject.service.TaskUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/task-uploads")
@RequiredArgsConstructor
public class TaskUploadController {
    private final TaskUploadService taskUploadService;
    private final TaskUploadMapper taskUploadMapper;
    @GetMapping("/{id}")
    public ResponseEntity<TaskUploadDto> getById(@PathVariable Long id, Authentication auth){
        return ResponseEntity.ok(taskUploadMapper.toDto(taskUploadService.getById(id,auth)));
    }

    @GetMapping("/{taskId}/all")
    public ResponseEntity<List<TaskUploadDto>> getByTaskId(@PathVariable Long taskId,Authentication auth){
        return ResponseEntity.ok(taskUploadMapper.toListDto(taskUploadService.getByTask(taskId,auth)));
    }

    @PostMapping(value = "/upload/{taskId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TaskUploadDto> upload(@PathVariable Long taskId,
                                               Authentication auth,
                                               @RequestParam("file") MultipartFile file){
        return ResponseEntity.ok(taskUploadMapper.toDto(taskUploadService.upload(taskId,file,auth)));
    }

    @DeleteMapping("/delete/{taskUploadId}")
    public ResponseEntity<String> delete(@PathVariable Long taskUploadId,
                                         Authentication auth){
        return ResponseEntity.ok(taskUploadService.delete(taskUploadId,auth));
    }
}
