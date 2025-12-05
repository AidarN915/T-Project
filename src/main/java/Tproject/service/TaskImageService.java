package Tproject.service;

import Tproject.model.TaskImage;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskImageService {
    public TaskImage getById(Long id, Authentication auth);
    public List<TaskImage> getByTask(Long taskId, Authentication auth);
    public TaskImage upload(Long taskId,MultipartFile file,Authentication auth);
    public String delete(Long id,Authentication auth);
}
