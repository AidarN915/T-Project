package Tproject.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtDto {
    private String token;
    private String refreshToken;
}
