package Tproject.dto;

import lombok.Data;

@Data
public class JwtDto {
    private String token;
    private String refreshToken;
}
