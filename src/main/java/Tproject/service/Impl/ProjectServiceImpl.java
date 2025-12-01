package Tproject.service.Impl;

import Tproject.dto.ProjectUpdateDto;
import Tproject.enums.OperationType;
import Tproject.model.Project;
import Tproject.model.User;
import Tproject.repository.ProjectRepository;
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
    private final UserUtil userUtil;
    private final UserRepository userRepository;
    private final CustomPermissionEvaluator permissionEvaluator;

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
            Set<Project> projects = new HashSet<>();
            if(user.getViewProjects() != null) projects.addAll(user.getViewProjects());
            if(user.getExecuteProjects() != null) projects.addAll(user.getExecuteProjects());
            if(user.getModerateProjects() != null) projects.addAll(user.getModerateProjects());
            return new ArrayList<>(projects);
        }
    }

    @Override
    public Project create(Authentication auth, String title) {
        Project project = new Project();
        project.setTitle(title);
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        if(!project.getViewers().add(user)
                || !project.getExecutors().add(user)
                || !project.getModerators().add(user)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Не удалось добавить пользователя");
        }
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
        if(updateDto.getViewers() != null) {
            Set<User> viewers = updateDto.getViewers().stream()
                    .map(dto -> userRepository.findByUsername(dto.getUsername())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден")))
                    .collect(Collectors.toSet());
            project.getViewers().clear();
            project.getViewers().addAll(viewers);
        }
        if(updateDto.getExecutors() != null) {
            Set<User> executors = updateDto.getExecutors().stream()
                    .map(dto -> userRepository.findByUsername(dto.getUsername())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден")))
                    .collect(Collectors.toSet());
            project.getExecutors().clear();
            project.getExecutors().addAll(executors);
        }
        if(updateDto.getModerators() != null) {
            Set<User> moderators = updateDto.getModerators().stream()
                    .map(dto -> userRepository.findByUsername(dto.getUsername())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден")))
                    .collect(Collectors.toSet());
            project.getModerators().clear();
            project.getModerators().addAll(moderators);
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
}
