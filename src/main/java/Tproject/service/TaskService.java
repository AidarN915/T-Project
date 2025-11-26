package Tproject.service;

import Tproject.dto.TaskCreateDto;
import Tproject.model.Task;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface TaskService {
    public Task create(Long taskListId,TaskCreateDto createDto,HttpServletRequest request);
    public List<Task> getByTaskListId(Long taskListId);
    public Task update(Long id,TaskCreateDto createDto,HttpServletRequest request);
    public Task changeStatus(Long id,boolean isDone,HttpServletRequest request);
    public String deleteTask(Long id,HttpServletRequest request);
    public Task getById(Long id);
}
