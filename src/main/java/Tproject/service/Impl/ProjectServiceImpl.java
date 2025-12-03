package Tproject.service.Impl;

import Tproject.dto.ProjectUpdateDto;
import Tproject.dto.UserDto;
import Tproject.enums.OperationType;
import Tproject.enums.UserProjectRoles;
import Tproject.model.Project;
import Tproject.model.ProjectsUsers;
import Tproject.model.User;
import Tproject.repository.ProjectRepository;
import Tproject.repository.ProjectsUsersRepository;
import Tproject.repository.UserRepository;
import Tproject.security.CustomPermissionEvaluator;
import Tproject.security.Target;
import Tproject.service.ProjectService;
import Tproject.util.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CustomPermissionEvaluator permissionEvaluator;
    private final ProjectsUsersRepository projectsUsersRepository;

    @Override
    public Project getById(Long id, Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth, Target.project(id,OperationType.READ))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Проект не найден"));
    }

    @Override
    public List<Project> getAll(Authentication auth) {
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        if(user.getRole().equals("SUPERADMIN") || user.getRole().equals("ADMIN")) {
            return projectRepository.findAll();
        }else {
            return projectsUsersRepository
                    .findByUser(user)
                    .stream()
                    .map(ProjectsUsers::getProject).distinct().collect(Collectors.toList());
        }
    }

    @Override
    public Project create(Authentication auth, String title) {
        Project project = new Project();
        project.setTitle(title);
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        ProjectsUsers pu = new ProjectsUsers();
        pu.setUser(user);
        pu.setProject(project);
        pu.setRole(UserProjectRoles.MODERATOR);
        project.getProjectsUsers().add(pu);
        projectRepository.save(project);
        return project;

    }

    @Override
    public Project update(Long projectId, Authentication auth, ProjectUpdateDto updateDto) {
        if(!permissionEvaluator.hasAccess(auth,Target.project(projectId, OperationType.MODIFY))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Проект не найден"));
        project.setTitle(updateDto.getTitle());
        if(updateDto.getViewers() != null || updateDto.getExecutors() != null || updateDto.getModerators() != null) {
            project.getProjectsUsers().clear();
        }
        if(updateDto.getViewers() != null) {
            addUsersToProject(project, updateDto.getViewers(), UserProjectRoles.VIEWER);
        }
        if(updateDto.getExecutors() != null) {
            addUsersToProject(project, updateDto.getExecutors(), UserProjectRoles.EXECUTOR);
        }
        if(updateDto.getModerators() != null) {
            addUsersToProject(project, updateDto.getModerators(), UserProjectRoles.MODERATOR);
        }
        projectRepository.save(project);
        return project;
    }

    @Override
    public String delete(Long projectId, Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth,Target.project(projectId,OperationType.MODIFY))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Проект не найден"));
        projectRepository.delete(project);
        return "Удалено";
    }

    private void addUsersToProject(Project project, List<UserDto> dtos, UserProjectRoles role) {
        if (dtos == null) return;

        List<ProjectsUsers> pus = dtos.stream()
                .map(dto -> {
                    User user = userRepository.findByUsername(dto.getUsername())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
                    ProjectsUsers pu = new ProjectsUsers();
                    pu.setProject(project);
                    pu.setUser(user);
                    pu.setRole(role);
                    return pu;
                })
                .toList();

        project.getProjectsUsers().addAll(pus);
    }

}
