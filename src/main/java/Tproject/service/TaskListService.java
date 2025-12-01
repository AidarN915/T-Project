package Tproject.service;

import Tproject.dto.TaskListDto;
import Tproject.model.TaskList;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TaskListService {
    public TaskList create(Long boardId,String title,  Authentication auth);
    public List<TaskList> getByBoardId(Long boardId, Authentication auth);
    public List<TaskList> all( Authentication auth);
    public TaskList update(String title,Long taskListId, Authentication auth);
    public String delete(Long taskListId, Authentication auth);
}
