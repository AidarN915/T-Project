package Tproject.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectDto {
    private Long id;
    private String title;
    private List<UserDto> viewers = new ArrayList<>();
    private List<UserDto> executors = new ArrayList<>();
    private List<UserDto> moderators = new ArrayList<>();
    private List<BoardDto> boards;
}
