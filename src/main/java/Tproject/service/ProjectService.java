package Tproject.service;

import Tproject.dto.ProjectDto;
import Tproject.dto.ProjectUpdateDto;
import Tproject.model.Project;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ProjectService {
    public Project getById(Long id, Authentication auth);
    public List<Project> getAll(Authentication auth);
    public Project create(Authentication auth,String title);
    public Project update(Long projectId, Authentication auth, ProjectUpdateDto updateDto);
    public String delete(Long projectId,Authentication auth);
}
