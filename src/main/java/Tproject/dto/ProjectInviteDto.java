package Tproject.dto;

import Tproject.enums.UserProjectRoles;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProjectInviteDto {
    private Integer countOfUses;
    private Integer expireHours;
    private UserProjectRoles role;
}
