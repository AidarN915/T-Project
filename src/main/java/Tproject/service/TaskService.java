package Tproject.service;

import Tproject.dto.TaskCreateDto;
import Tproject.model.Task;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TaskService {
    public Task create(Long taskListId, TaskCreateDto createDto, Authentication auth);
    public List<Task> getByTaskListId(Long taskListId,Authentication auth);
    public Task update(Long id,TaskCreateDto createDto,Authentication auth);
    public Task changeStatus(Long id,boolean isDone,Authentication auth);
    public String deleteTask(Long id,Authentication auth);
    public Task getById(Long id,Authentication auth);
}
