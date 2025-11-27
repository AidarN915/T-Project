package Tproject.service;

import Tproject.dto.TaskListDto;
import Tproject.model.TaskList;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface TaskListService {
    public TaskList create(Long boardId,String title, HttpServletRequest request);
    public List<TaskList> all(Long boardId,HttpServletRequest request);
    public TaskList update(String title,Long taskListId,HttpServletRequest request);
    public String delete(Long taskListId,HttpServletRequest request);
}
