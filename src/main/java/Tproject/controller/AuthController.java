package Tproject.controller;

import Tproject.dto.AuthDto;
import Tproject.dto.JwtDto;
import Tproject.dto.UserDto;
import Tproject.mapper.UserMapper;
import Tproject.model.User;
import Tproject.service.AuthService;
import Tproject.util.RefreshTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import Tproject.repository.UserRepository;
import Tproject.service.UserDetailsServiceImpl;
import Tproject.util.JwtUtil;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserMapper userMapper;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@RequestBody AuthDto authDto){
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
