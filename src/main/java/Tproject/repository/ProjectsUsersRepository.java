package Tproject.repository;

import Tproject.enums.UserProjectRoles;
import Tproject.model.Project;
import Tproject.model.ProjectsUsers;
import Tproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.management.relation.Role;
import java.util.List;

public interface ProjectsUsersRepository extends JpaRepository<ProjectsUsers, Long> {
    public List<ProjectsUsers> findByUserAndProjectAndRole(User user, Project project, UserProjectRoles role);
    public List<ProjectsUsers> findByUser(User user);
    public List<ProjectsUsers> findByProject(Project project);
    public List<ProjectsUsers> findByRole(Role role);
    public List<ProjectsUsers> findByRoleAndUser(UserProjectRoles role,User user);

    boolean existsByUserAndProject(User user,Project project);
    boolean existsByUserAndProjectAndRole(User user, Project project, UserProjectRoles role);
}
