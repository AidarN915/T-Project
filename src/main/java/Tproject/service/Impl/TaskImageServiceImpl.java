package Tproject.service.Impl;

import Tproject.enums.OperationType;
import Tproject.model.Task;
import Tproject.model.TaskImage;
import Tproject.repository.TaskImageRepository;
import Tproject.repository.TaskRepository;
import Tproject.repository.UserRepository;
import Tproject.security.CustomPermissionEvaluator;
import Tproject.security.Target;
import Tproject.service.ChatService;
import Tproject.service.TaskImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
public class TaskImageServiceImpl implements TaskImageService {
    private final TaskRepository taskRepository;
    private final TaskImageRepository taskImageRepository;
    private final CustomPermissionEvaluator permissionEvaluator;
    private final ChatService chatService;
    private final UserRepository userRepository;
    @Value("${uploads.images}")
    private String imagesPath;
    @Override
    public TaskImage getById(Long id, Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth, Target.taskImage(id, OperationType.READ))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return taskImageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Изображение не найдено"));
    }

    @Override
    public List<TaskImage> getByTask(Long taskId, Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth,Target.task(taskId,OperationType.READ))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Задача не найдена"))
                .getTaskImages();
    }

    @Override
    public TaskImage upload(Long taskId,MultipartFile file, Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth,Target.task(taskId,OperationType.MODIFY))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Задача не найдена"));

        String ext = Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = task.getId() + "_" + UUID.randomUUID() + ext;
        File dir = new File(imagesPath);
        if (!dir.exists()) dir.mkdirs();


        try {
            file.transferTo(new File(dir, fileName));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Не удалось сохранить файл");
        }

        String url = "/images/" + fileName;
        TaskImage taskImage = new TaskImage();
        taskImage.setImageUrl(url);
        taskImage.setTask(task);
        taskImageRepository.save(taskImage);

        chatService.sendEventMessage(task.getChatRoom().getId(),
                "Пользователь " + auth.getName() + " загрузил изображение",
                auth);
        return taskImage;
    }

    @Override
    public String delete(Long id, Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth,Target.taskImage(id,OperationType.MODIFY))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        TaskImage taskImage = taskImageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Изображение на найдено"));
        taskImageRepository.delete(taskImage);
        return "Удалено";
    }
}
