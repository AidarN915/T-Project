package Tproject.mapper;

import Tproject.dto.BoardDto;
import Tproject.dto.ProjectDto;
import Tproject.model.Board;
import Tproject.model.Project;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",uses = {UserMapper.class,BoardMapper.class})
public abstract class ProjectMapper {
    public abstract ProjectDto toDto(Project board);
    public abstract List<ProjectDto> toListDto(List<Project> boards);
}
