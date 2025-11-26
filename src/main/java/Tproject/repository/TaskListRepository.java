package Tproject.repository;

import Tproject.model.TaskList;
import Tproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskListRepository extends JpaRepository<TaskList,Long> {
    public List<TaskList> getByUser(User user);
}
