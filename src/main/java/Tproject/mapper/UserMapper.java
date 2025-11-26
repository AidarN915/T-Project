package Tproject.mapper;

import Tproject.dto.UserDto;
import Tproject.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    public abstract UserDto toDto(User user);
}
