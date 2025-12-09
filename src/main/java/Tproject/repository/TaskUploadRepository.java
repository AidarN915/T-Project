package Tproject.repository;

import Tproject.model.TaskUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskUploadRepository extends JpaRepository<TaskUpload,Long> {
}
