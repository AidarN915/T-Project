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
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@RequestBody AuthDto authDto){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authDto.getUsername(),
                authDto.getPassword()
        ));
        UserDetails userDetails = userDetailsService.loadUserByUsername(authDto.getUsername());
        String refreshToken = RefreshTokenUtil.generateRefreshToken();
        String token = jwtUtil.generateToken(userDetails);

        authService.setRefreshToken(authDto.getUsername(), refreshToken);

        JwtDto jwt = new JwtDto();
        jwt.setRefreshToken(refreshToken);
        jwt.setToken(token);
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody AuthDto user) {
        Tproject.model.User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(newUser);
        UserDto dto = userMapper.toDto(newUser);
        return ResponseEntity.ok(userMapper.toDto(newUser));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);

        String username = jwtUtil.extractUsername(token);

        authService.logout(username);

        return ResponseEntity.ok("Ок");
    }
    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refresh(@RequestParam("refreshToken") String refreshToken){
        JwtDto dto = authService.refresh(refreshToken);
        return ResponseEntity.ok(dto);
    }
}
