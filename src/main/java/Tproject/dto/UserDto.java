package Tproject.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String avatarUrl;
    public UserDto(String username){
        this.username = username;
    }
    public UserDto(){
    }
}
