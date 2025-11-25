package Tproject.dto;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    public UserDto(String username){
        this.username = username;
    }
    public UserDto(){
    }
}
