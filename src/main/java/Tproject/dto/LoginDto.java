package Tproject.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String role;
    private String token;
    private String refreshToken;
}
