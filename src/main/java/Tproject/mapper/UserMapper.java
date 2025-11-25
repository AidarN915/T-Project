package Tproject.mapper;

import Tproject.dto.UserDto;
import Tproject.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    public UserDto toDto(User user);
}
