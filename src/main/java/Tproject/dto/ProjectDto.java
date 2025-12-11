package Tproject.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProjectDto extends AuditableDto {
    private Long id;
    private String title;
    private List<UserDto> viewers = new ArrayList<>();
    private List<UserDto> executors = new ArrayList<>();
    private List<UserDto> moderators = new ArrayList<>();
    private List<BoardDto> boards;
}
