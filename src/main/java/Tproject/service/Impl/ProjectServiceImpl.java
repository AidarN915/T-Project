package Tproject.service.Impl;

import Tproject.dto.ProjectInviteDto;
import Tproject.dto.ProjectUpdateDto;
import Tproject.dto.UserDtoRequest;
import Tproject.enums.OperationType;
import Tproject.enums.UserProjectRoles;
import Tproject.model.Project;
import Tproject.model.ProjectInvite;
import Tproject.model.ProjectsUsers;
import Tproject.model.User;
import Tproject.repository.ProjectInviteRepository;
import Tproject.repository.ProjectRepository;
import Tproject.repository.ProjectsUsersRepository;
import Tproject.repository.UserRepository;
import Tproject.security.CustomPermissionEvaluator;
import Tproject.security.Target;
import Tproject.service.ProjectService;
import Tproject.util.GenerateTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CustomPermissionEvaluator permissionEvaluator;
    private final ProjectsUsersRepository projectsUsersRepository;
    private final ProjectInviteRepository projectInviteRepository;

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
            List<Project> projects = projectsUsersRepository
                    .findByUser(user)
                    .stream()
                    .map(ProjectsUsers::getProject).distinct().toList();
            projects.forEach(project ->
                    project.getBoards().forEach(board ->
                            board.getTaskLists().forEach(taskList ->
                                    taskList.getTasks().removeIf(task -> task.getParentTask() != null)
                            )
                    )
            );
            return projects;
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
        project.markAsDeleted();
        projectRepository.save(project);
        return "Удалено";
    }

    @Override
    public String generateProjectInvite(Long projectId,ProjectInviteDto projectInviteDto, Authentication auth) {
        if(!permissionEvaluator.hasAccess(auth,Target.project(projectId, OperationType.MODIFY))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        ProjectInvite projectInvite = new ProjectInvite();
        if(projectInviteDto.getExpireHours() != null){
            projectInvite.setExpiresAt(LocalDateTime.now().plusHours(projectInviteDto.getExpireHours()));
        }
        if(projectInviteDto.getCountOfUses() != null){
            projectInvite.setCount(projectInviteDto.getCountOfUses());
        }
        projectInvite.setRole(projectInviteDto.getRole());
        Project project = projectRepository.findById(projectId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Проект не найден"));
        projectInvite.setProject(project);
        String token = GenerateTokenUtil.generateInviteToken();
        projectInvite.setToken(token);
        projectInviteRepository.save(projectInvite);
        return token;
    }

    @Override
    @Transactional
    public Project acceptInvite(String token, Authentication auth) {
        ProjectInvite projectInvite = projectInviteRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Приглашение не найдено"));
        if(projectInvite.getCount() != null && projectInvite.getCount() < 1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Количество использований закончилось");
        }
        if(projectInvite.getExpiresAt() != null && projectInvite.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Приглашение более не действительно");
        }
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        ProjectsUsers pu = new ProjectsUsers();
        if(projectsUsersRepository.existsByUserAndProject(user,projectInvite.getProject())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Вы уже участвуете в этом проекте");
        }
        pu.setRole(projectInvite.getRole());
        pu.setProject(projectInvite.getProject());
        pu.setUser(user);
        if (projectInvite.getCount() != null) {
            projectInvite.setCount(projectInvite.getCount() - 1);
        }
        projectsUsersRepository.save(pu);
        return projectInvite.getProject();
    }

    private void addUsersToProject(Project project, List<UserDtoRequest> dtos, UserProjectRoles role) {
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
