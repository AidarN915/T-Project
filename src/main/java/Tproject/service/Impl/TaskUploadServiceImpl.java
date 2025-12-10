package Tproject.service.Impl;

import Tproject.enums.MessageType;
import Tproject.enums.OperationType;
import Tproject.model.Task;
import Tproject.model.TaskUpload;
import Tproject.repository.TaskUploadRepository;
import Tproject.repository.TaskRepository;
import Tproject.repository.UserRepository;
import Tproject.security.CustomPermissionEvaluator;
import Tproject.security.Target;
import Tproject.service.ChatService;
import Tproject.service.TaskUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskUploadServiceImpl implements TaskUploadService {
    private final TaskRepository taskRepository;
    private final TaskUploadRepository taskUploadRepository;
    private final CustomPermissionEvaluator permissionEvaluator;
    private final ChatService chatService;
    private final UserRepository userRepository;
    @Value("${uploads.files}")
    private String filePath;
    @Override
    public TaskUpload getById(Long id, Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth, Target.taskUpload(id, OperationType.READ))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return taskUploadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Изображение не найдено"));
    }

    @Override
    public List<TaskUpload> getByTask(Long taskId, Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth,Target.task(taskId,OperationType.READ))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Задача не найдена"))
                .getTaskUploads();
    }

    @Override
    public TaskUpload upload(Long taskId,MultipartFile file, Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth,Target.task(taskId,OperationType.MODIFY))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Задача не найдена"));

        String ext = Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = task.getId() + "_" + UUID.randomUUID() + ext;
        File dir = new File(filePath);
        if (!dir.exists()) dir.mkdirs();


        try {
            file.transferTo(new File(dir, fileName));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Не удалось сохранить файл");
        }

        String url = "/files/" + fileName;
        TaskUpload taskUpload = new TaskUpload();
        taskUpload.setUrl(url);
        taskUpload.setTask(task);
        taskUploadRepository.save(taskUpload);

        chatService.sendMessage(task.getChatRoom().getId(),
                url,
                MessageType.FILE,
                auth);
        return taskUpload;
    }

    @Override
    public String delete(Long id, Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth,Target.taskUpload(id,OperationType.MODIFY))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        TaskUpload taskUpload = taskUploadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Изображение на найдено"));
        taskUpload.markAsDeleted();
        taskUploadRepository.save(taskUpload);
        return "Удалено";
    }
}
