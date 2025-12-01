package Tproject.controller;

import Tproject.dto.AuthDto;
import Tproject.dto.JwtDto;
import Tproject.dto.LoginDto;
import Tproject.dto.UserDto;
import Tproject.mapper.UserMapper;
import Tproject.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserMapper userMapper;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginDto> login(@RequestBody AuthDto authDto){
        return ResponseEntity.ok(authService.login(authDto));

    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody AuthDto user) {
        return ResponseEntity.ok(userMapper.toDto(authService.create(user)));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.ok("Ок");
    }
    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refresh(@RequestParam("refreshToken") String refreshToken){
        JwtDto dto = authService.refresh(refreshToken);
        return ResponseEntity.ok(dto);
    }
}
