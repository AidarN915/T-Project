package Tproject.service;

import Tproject.dto.TaskCreateDto;
import Tproject.model.Task;

public interface TaskService {
    public Task create(TaskCreateDto createDto);
    public Task update(Integer id,TaskCreateDto createDto);
    public Task changeStatus(Integer id,boolean isDone);
    public String deleteTask(Integer id);
}
