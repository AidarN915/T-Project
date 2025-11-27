package Tproject.service;

import Tproject.dto.ProjectDto;
import Tproject.dto.ProjectUpdateDto;
import Tproject.model.Project;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ProjectService {
    public Project getById(Long id, HttpServletRequest request);
    public List<Project> getAll(HttpServletRequest request);
    public Project create(HttpServletRequest request,String title);
    public Project update(Long projectId, HttpServletRequest request, ProjectUpdateDto updateDto);
    public String delete(Long projectId,HttpServletRequest request);
}
