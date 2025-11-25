package Tproject.service;

import Tproject.dto.JwtDto;
import Tproject.model.User;
import Tproject.repository.UserRepository;
import Tproject.util.JwtUtil;
import Tproject.util.RefreshTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    @Override
    public String logout(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        user.setRefreshToken(null);
        userRepository.save(user);
        return "Ок";
    }

    @Override
    public String setRefreshToken(String username, String refreshToken) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        return "Ок";
    }

    @Override
    public JwtDto refresh(String refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        String newRefreshToken = RefreshTokenUtil.generateRefreshToken();
        user.setRefreshToken(newRefreshToken);
        String jwtToken = jwtUtil.generateToken(userDetailsService.loadUserByUsername(user.getUsername()));
        JwtDto dto = new JwtDto();
        dto.setRefreshToken(refreshToken);
        dto.setToken(jwtToken);
        return dto;
    }
}
