package Tproject.service;

import Tproject.model.TaskUpload;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskUploadService {
    public TaskUpload getById(Long id, Authentication auth);
    public List<TaskUpload> getByTask(Long taskId, Authentication auth);
    public TaskUpload upload(Long taskId,MultipartFile file,Authentication auth);
    public String delete(Long id,Authentication auth);
}
