package Tproject.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    private Long id;
    private String username;
    private String role;
    private String token;
    private String refreshToken;
    private String avatarUrl;
    private String phoneNumber;
}
