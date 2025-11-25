package Tproject.service;


import Tproject.dto.JwtDto;

public interface AuthService {
    public String logout(String username);
    public String setRefreshToken(String username,String refreshToken);
    public JwtDto refresh(String refreshToken);
}
