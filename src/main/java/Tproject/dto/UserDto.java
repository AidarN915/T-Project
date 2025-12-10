package Tproject.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String avatarUrl;
    private String phoneNumber;
}
