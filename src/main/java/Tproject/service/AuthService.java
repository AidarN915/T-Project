package Tproject.service;


import Tproject.dto.AuthDto;
import Tproject.dto.JwtDto;
import Tproject.model.User;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    public void logout(HttpServletRequest request);
    public JwtDto refresh(String refreshToken);
    public JwtDto login(AuthDto authDto);
    public User create(AuthDto authDto);
}
