package Tproject.service;

import Tproject.dto.LoginDto;
import Tproject.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    public List<User> getAll();
    public User setRole(String username, String newRole, HttpServletRequest request);
    public LoginDto updateName(String username, Authentication auth);
    public User updatePhoneNumber(String phoneNumber,Authentication auth);
    public User uploadAvatar(MultipartFile file,Authentication auth);
}
