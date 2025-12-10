package Tproject.dto;

import Tproject.model.User;
import lombok.Data;

import java.util.List;

@Data
public class ProjectUpdateDto {
    private String title;
    private List<UserDtoRequest> viewers;
    private List<UserDtoRequest> executors;
    private List<UserDtoRequest> moderators;
}
