package Tproject.repository;

import Tproject.model.Task;
import Tproject.model.TaskImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskImageRepository extends JpaRepository<TaskImage,Long> {
}
