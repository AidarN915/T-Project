package Tproject.service.Impl;

import Tproject.dto.ProjectUpdateDto;
import Tproject.model.Project;
import Tproject.model.User;
import Tproject.repository.ProjectRepository;
import Tproject.repository.UserRepository;
import Tproject.service.ProjectService;
import Tproject.util.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserUtil userUtil;
    private final UserRepository userRepository;

    @Override
    public Project getById(Long id, HttpServletRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Проект не найден"));
        User user = userUtil.getUserByRequest(request);
        if(!project.getUsers().contains(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return project;
    }

    @Override
    public List<Project> getAll(HttpServletRequest request) {
        User user = userUtil.getUserByRequest(request);
        return user.getProjects().stream().toList();
    }

    @Override
    public Project create(HttpServletRequest request, String title) {
        Project project = new Project();
        project.setTitle(title);
        User user = userUtil.getUserByRequest(request);
        if(!project.getUsers().add(user)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Не удалось добавить пользователя");
        }
        projectRepository.save(project);
        return project;

    }

    @Override
    public Project update(Long projectId, HttpServletRequest request, ProjectUpdateDto updateDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Проект не найден"));
        User user = userUtil.getUserByRequest(request);
        if(!project.getUsers().contains(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        project.setTitle(updateDto.getTitle());

        Set<User> users = updateDto.getUsers().stream()
                .map(dto -> userRepository.findByUsername(dto.getUsername())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден")))
                .collect(Collectors.toSet());
        project.getUsers().clear();
        project.getUsers().addAll(users);
        projectRepository.save(project);
        return project;
    }

    @Override
    public String delete(Long projectId, HttpServletRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Проект не найден"));
        User user = userUtil.getUserByRequest(request);
        if(!project.getUsers().contains(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        projectRepository.delete(project);
        return "Удалено";
    }
}
