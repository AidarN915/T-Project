package Tproject.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProjectDto {
    private Long id;
    private String title;
    private List<UserDto> viewers;
    private List<UserDto> executors;
    private List<UserDto> moderators;
    private List<BoardDto> boards;
}
