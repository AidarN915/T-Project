package Tproject.service;

import Tproject.dto.TaskCreateDto;
import Tproject.model.Task;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface TaskService {
    public Task create(TaskCreateDto createDto,HttpServletRequest request);
    public List<Task> getTasksByRequest(HttpServletRequest request);
    public Task update(Long id,TaskCreateDto createDto);
    public Task changeStatus(Long id,boolean isDone);
    public String deleteTask(Long id);
    public Task getById(Long id);
}
