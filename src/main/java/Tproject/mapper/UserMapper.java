package Tproject.mapper;

import Tproject.dto.JwtDto;
import Tproject.dto.LoginDto;
import Tproject.dto.UserDto;
import Tproject.dto.UserListDto;
import Tproject.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    public abstract UserDto toDto(User user);
    public abstract List<UserDto> toListDto(List<User> users);
    public abstract List<UserListDto> toListRoleDto(List<User> users);
    public LoginDto toLoginDto(User user, JwtDto jwt){
        LoginDto dto = new LoginDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setToken(jwt.getToken());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setRefreshToken(jwt.getRefreshToken());
        dto.setPhoneNumber(user.getPhoneNumber());
        return dto;
    }
}
