package Tproject.dto;

import Tproject.model.User;
import lombok.Data;

import java.util.List;

@Data
public class ProjectUpdateDto {
    private String title;
    private List<UserDto> viewers;
    private List<UserDto> executors;
    private List<UserDto> moderators;
}
