package Tproject.dto;

import Tproject.model.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectUpdateDto {
    private String title;
    private List<UserDtoRequest> viewers;
    private List<UserDtoRequest> executors;
    private List<UserDtoRequest> moderators;
}
