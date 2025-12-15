package Tproject.repository;

import Tproject.model.ProjectInvite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectInviteRepository extends JpaRepository<ProjectInvite,Long> {
    public Optional<ProjectInvite> findByToken(String token);
}
