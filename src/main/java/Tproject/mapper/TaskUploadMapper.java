package Tproject.mapper;

import Tproject.dto.TaskUploadDto;
import Tproject.model.TaskUpload;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TaskUploadMapper {
    public abstract TaskUploadDto toDto(TaskUpload taskUpload);
    public abstract List<TaskUploadDto> toListDto(List<TaskUpload> taskUploads);
}
