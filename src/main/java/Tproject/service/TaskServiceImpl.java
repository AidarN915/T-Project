package Tproject.service;

import Tproject.dto.TaskCreateDto;
import Tproject.model.Task;

public class TaskServiceImpl implements TaskService{
    @Override
    public Task create(TaskCreateDto createDto) {
        return null;
    }

    @Override
    public Task update(Integer id, TaskCreateDto createDto) {
        return null;
    }

    @Override
    public Task changeStatus(Integer id, boolean isDone) {
        return null;
    }

    @Override
    public String deleteTask(Integer id) {
        return "";
    }
}
