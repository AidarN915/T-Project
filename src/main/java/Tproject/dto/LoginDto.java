package Tproject.dto;

import lombok.Data;

@Data
public class LoginDto {
    private Long id;
    private String username;
    private String role;
    private String token;
    private String refreshToken;
    private String avatarUrl;
    private String phoneNumber;
}
