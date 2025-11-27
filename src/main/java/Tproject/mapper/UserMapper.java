package Tproject.mapper;

import Tproject.dto.UserDto;
import Tproject.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    public abstract UserDto toDto(User user);
    public abstract List<UserDto> toListDto(List<User> users);
}
