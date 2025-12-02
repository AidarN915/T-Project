package Tproject.mapper;

import Tproject.dto.ProjectDto;
import Tproject.model.Project;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring",uses = {UserMapper.class,BoardMapper.class})
public abstract class ProjectMapper {
    public abstract ProjectDto toDto(Project board);
    public abstract List<ProjectDto> toListDto(List<Project> boards);

    @Autowired
    protected UserMapper userMapper;

    @AfterMapping
    protected void enrich(@MappingTarget ProjectDto dto, Project project) {

        project.getProjectsUsers().forEach(pu -> {
            var userDto = userMapper.toDto(pu.getUser());

            switch (pu.getRole()) {
                case VIEWER:
                    dto.getViewers().add(userDto);
                    break;
                case EXECUTOR:
                    dto.getExecutors().add(userDto);
                    break;
                case MODERATOR:
                    dto.getModerators().add(userDto);
                    break;
            }
        });
    }
}
