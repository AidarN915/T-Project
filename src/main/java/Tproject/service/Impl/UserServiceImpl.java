package Tproject.service.Impl;

import Tproject.dto.JwtDto;
import Tproject.dto.LoginDto;
import Tproject.dto.UserDto;
import Tproject.mapper.UserMapper;
import Tproject.model.User;
import Tproject.repository.UserRepository;
import Tproject.service.UserService;
import Tproject.util.JwtUtil;
import Tproject.util.RefreshTokenUtil;
import Tproject.util.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserUtil userUtil;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserMapper userMapper;

    @Value("${uploads.avatars}")
    private String avatarsPath;

    @Value("${jwt.rtExpiration}")
    private Integer refreshTokenExpiration;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
    @Override
    public User setRole(String username, String newRole, HttpServletRequest request){
        User user = userUtil.getUserByRequest(request);
        if(!user.getRole().equals("SUPERADMIN") || username.equals(user.getUsername())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (!newRole.equals("ADMIN") && !newRole.equals("USER")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        User newUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        newUser.setRole(newRole);
        userRepository.save(newUser);
        return user;
    }
    @Override
    @Transactional
    public LoginDto updateName(String username, Authentication auth){
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        if(userRepository.existsByUsername(username)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Данное имя пользователя занято");
        }
        user.setUsername(username);

        String newRefreshToken = RefreshTokenUtil.generateRefreshToken();
        user.setRefreshToken(newRefreshToken);
        user.setRefreshTokenExpires(LocalDateTime.now().plusHours(refreshTokenExpiration));

        userRepository.save(user);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        auth.getCredentials(),
                        auth.getAuthorities()
                )
        );

        String jwtToken = jwtUtil.generateToken(userDetailsService.loadUserByUsername(user.getUsername()));
        JwtDto jwt = new JwtDto();
        jwt.setToken(jwtToken);
        jwt.setRefreshToken(newRefreshToken);
        return userMapper.toLoginDto(user,jwt);
    }

    @Override
    public User updatePhoneNumber(String phoneNumber, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
        return user;
    }

    @Override
    public User uploadAvatar(MultipartFile file, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Пользователь не найден"));

        String ext = Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = user.getId() + "_" + UUID.randomUUID() + ext;
        File dir = new File(avatarsPath);
        if (!dir.exists()) dir.mkdirs();

        try {
            file.transferTo(new File(dir, fileName));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Не удалось сохранить файл");
        }
        String url = "/avatars/" + fileName;
        user.setAvatarUrl(url);
        userRepository.save(user);
        return user;
    }
}
