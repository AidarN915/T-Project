package Tproject.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProjectDto {
    private Long id;
    private String title;
    private List<UserDto> users;
    private List<BoardDto> boards;
}
