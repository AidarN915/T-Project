package Tproject.service.Impl;

import Tproject.dto.AuthDto;
import Tproject.dto.JwtDto;
import Tproject.model.User;
import Tproject.repository.UserRepository;
import Tproject.service.AuthService;
import Tproject.util.JwtUtil;
import Tproject.util.RefreshTokenUtil;
import Tproject.util.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserUtil userUtil;
    @Value("${jwt.rtExpirationDays}")
    private Integer refreshTokenExpiration;


    @Override
    public void logout(HttpServletRequest request) {
        User user = userUtil.getUserByRequest(request);
        user.setRefreshToken(null);
        user.setRefreshTokenExpires(null);
        userRepository.save(user);
    }

    @Override
    public JwtDto refresh(String refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        if(user.getRefreshTokenExpires().isBefore(LocalDateTime.now())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        String newRefreshToken = RefreshTokenUtil.generateRefreshToken();
        user.setRefreshToken(newRefreshToken);
        user.setRefreshTokenExpires(LocalDateTime.now().plusDays(refreshTokenExpiration));
        userRepository.save(user);
        String jwtToken = jwtUtil.generateToken(userDetailsService.loadUserByUsername(user.getUsername()));
        JwtDto dto = new JwtDto();
        dto.setRefreshToken(newRefreshToken);
        dto.setToken(jwtToken);
        return dto;
    }

    @Override
    public JwtDto login(AuthDto authDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            authDto.getUsername(),
            authDto.getPassword()
        ));
        UserDetails userDetails = userDetailsService.loadUserByUsername(authDto.getUsername());
        String refreshToken = RefreshTokenUtil.generateRefreshToken();
        String token = jwtUtil.generateToken(userDetails);

        User user = userRepository.findByUsername(authDto.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpires(LocalDateTime.now().plusDays(refreshTokenExpiration));
        userRepository.save(user);

        JwtDto jwt = new JwtDto();
        jwt.setRefreshToken(refreshToken);
        jwt.setToken(token);
        return jwt;
    }

    @Override
    public User create(AuthDto authDto) {
        if(userRepository.existsByUsername(authDto.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Имя занято");
        }
        User newUser = new User();
        newUser.setUsername(authDto.getUsername());
        newUser.setPassword(passwordEncoder.encode(authDto.getPassword()));
        userRepository.save(newUser);
        return newUser;
    }
}
